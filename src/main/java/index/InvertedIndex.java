package index;

import utils.Normalizer;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class InvertedIndex {

    public static final String NO_RESULT = "No result";
    private AtomicLong crtId = new AtomicLong();

    private Map<Long, String> idToFile = new ConcurrentHashMap<>();
    private Map<String, Long> fileToId = new ConcurrentHashMap<>();

    private Map<String, SortedSet<DocToOccurrences>> index = new ConcurrentHashMap<>();

    private static InvertedIndex instance = new InvertedIndex();

    private InvertedIndex() {}

    public static InvertedIndex getInstance()  {
        return instance;
    }

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
            System.out.println("> Indexing " + absolutePath);
            Future<Map<String, Integer>> wordCounter = executorService.submit(new FileProcessingTask(f));
            executorService.submit(new IndexingTask(absolutePath, wordCounter));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // TODO - log error
        }
    }

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

    public String searchWordsInIndex(List<String> words) {
        List<SortedSet<DocToOccurrences>> allDocuments = new ArrayList<>();
        for (String word : words) {
            if (index.containsKey(word)) {
                allDocuments.add(index.get(word));
            }
        }
        if (allDocuments.isEmpty()) {
            return NO_RESULT;
        }
        if (allDocuments.size() == 1) {
            DocToOccurrences docToOccurrences =  allDocuments.get(0).first();
            return idToFile.get(docToOccurrences.getDocumentId());
        }
        TreeSet<DocToOccurrences> intersection = new TreeSet<>(allDocuments.get(0));

        for (int i = 1; i < allDocuments.size(); ++i) {
            intersection.retainAll(allDocuments.get(i));
        }

        if (intersection.isEmpty()) {
            return  NO_RESULT;
        }
        DocToOccurrences docToOccurrences =  intersection.first();
        return idToFile.get(docToOccurrences.getDocumentId());
    }
}