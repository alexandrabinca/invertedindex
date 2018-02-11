package index;

import filter.EnStopWordsFilter;
import filter.RoStopWordsFilter;
import index.task.FileProcessingTask;
import index.task.IndexingTask;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import processor.TextProcessor;
import processor.WordProcessor;
import stemmer.EnStemmer;
import stemmer.RoStemmer;
import utils.Language;
import utils.LanguageDetector;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * InvertedIndex - singleton.
 * Stores the inverted index as a map from Words to Ranked List of documents.
 * The ranking is possible because of {@link index.DocToOccurrences} object that implements comparable based on word occurrences.
 *
 * It also keeps the mappings from documents to internal ids and vice versa.
 */
public class InvertedIndex {
    private Logger logger = Logger.getLogger(InvertedIndex.class);

    private AtomicLong crtId = new AtomicLong();
    private Map<Long, String> idToFile = new ConcurrentHashMap<>();
    private Map<String, Long> fileToId = new ConcurrentHashMap<>();

    private Map<String, SortedSet<DocToOccurrences>> index = new ConcurrentHashMap<>();

    private static InvertedIndex instance = new InvertedIndex();

    private InvertedIndex() {}

    public static InvertedIndex getInstance()  {
        return instance;
    }

    /**
     * All the files that are found at the specified path are processed in parallel using executorService
     * and {@link index.task.FileProcessingTask}.
     * The {@link processor.TextProcessor} will detect the language and will create the proper
     * {@link processor.WordProcessor} that will help with all the operations needed (Normalization,
     * Stop words filtering, Stemming) for each token.
     * {@link index.task.FileProcessingTask} will return a future map from each word to words occurrences
     * that will be added to the index using {@link index.task.IndexingTask}.
     * @param path - the path of files to be indexed
     */
    public void addFilesToIndex(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException("The path is invalid");
        }
        Set<File> filesInPath = new HashSet<>();
        Queue<File> queue = new LinkedList<>();
        queue.add(file);
        while (!queue.isEmpty()) {
            File crtFile = queue.remove();
            if (crtFile.isDirectory()) {
                queue.addAll(Arrays.asList(file.listFiles()));
            } else {
                filesInPath.add(crtFile);
            }
        }
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (File f : filesInPath) {
            String absolutePath = f.getAbsolutePath();
            logger.info("Indexing " + absolutePath);
            Future<Map<String, Integer>> wordCounter = executorService.submit(new FileProcessingTask(f));
            executorService.submit(new IndexingTask(absolutePath, wordCounter));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            logger.error("Failed to stop executor service.", e);
        }
    }

    /**
     * Add a word to occurrences map to the index
     * @param filePath
     * @param wordCounter
     */
    public void addWordsToIndex(String filePath, Map<String, Integer> wordCounter) {
        if (!fileToId.containsKey(filePath)) {
            long id = crtId.incrementAndGet();
            idToFile.put(id, filePath);
            fileToId.put(filePath, id);
        }

        for (String word: wordCounter.keySet()) {
            Integer wordOccurrences = wordCounter.get(word);
            DocToOccurrences docToOccurrences = new DocToOccurrences(fileToId.get(filePath), wordOccurrences);
            if (!index.containsKey(word)) {
                index.put(word, Collections.synchronizedSortedSet(new TreeSet<>()));
            }
            SortedSet<DocToOccurrences> sortedSet = index.get(word);
            if (sortedSet.contains(docToOccurrences)) {
                // this could not happen in the current implementation
                // dto = sortedSet.get(docToOccurrences) - doesn't exist
                // workaround: use Map<DTO, DTO> instead of Set<DTO>
                for (DocToOccurrences dto : sortedSet) {
                    if (dto.equals(docToOccurrences)) {
                        dto.addToCount(wordOccurrences);
                    }
                }
            } else {
                sortedSet.add(docToOccurrences);
            }
        }
    }

    /**
     * process the user input in the same way that the files are processed (Normalization,
     * Stop words filtering, Stemming)
     * @param searchWords - user input
     * @return the list of files that contains user input in a ranked order
     */
    public List<String> getSearchResult(String searchWords) {
        List<String> result;
        if (Language.RO ==  LanguageDetector.detectLanguage(searchWords)) {
            List<String> words = new TextProcessor(searchWords, new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance())).getAllWords();
            result = searchWordsInIndex(words);
        } else {
            List<String> words = new TextProcessor(searchWords, new WordProcessor(EnStopWordsFilter.getInstance(), EnStemmer.getInstance())).getAllWords();
            result = searchWordsInIndex(words);
            if (result.isEmpty()) { // difficult to detect ro from few words
                words = new TextProcessor(searchWords, new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance())).getAllWords();
                result = searchWordsInIndex(words);
            }
        }
        return result;
    }

    private List<String> searchWordsInIndex(List<String> words) {
        List<SortedSet<DocToOccurrences>> allDocuments = new ArrayList<>();
        for (String word : words) {
            if (index.containsKey(word)) {
                allDocuments.add(index.get(word));
            }
        }
        if (allDocuments.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        if (allDocuments.size() == 1) {
            return getDocumentsInRankOrder(allDocuments.get(0));
        }
        Collection<DocToOccurrences> intersection = allDocuments.get(0);

        for (int i = 1; i < allDocuments.size(); ++i) {
            intersection = CollectionUtils.intersection(intersection, allDocuments.get(i));
        }

        if (intersection.isEmpty()) {
            return  Collections.EMPTY_LIST;
        }
        return getDocumentsInRankOrder(Collections.synchronizedSortedSet(new TreeSet<>(intersection)));
    }

    private List<String> getDocumentsInRankOrder(SortedSet<DocToOccurrences> documents) {
        List<String> docNames = new ArrayList<>();
        for (DocToOccurrences docToOccurrences: documents) {
            docNames.add(idToFile.get(docToOccurrences.getDocumentId()));
            //docNames.add(idToFile.get(docToOccurrences.getDocumentId()) + " (occurrences :" + docToOccurrences.getWordOccurrences() + ")");
        }
        return docNames;
    }
}
