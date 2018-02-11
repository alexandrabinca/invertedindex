import filter.EnStopWordsFilter;
import filter.RoStopWordsFilter;
import index.InvertedIndex;
import org.apache.log4j.Logger;
import processor.TextProcessor;
import processor.WordProcessor;
import stemmer.EnStemmer;
import stemmer.RoStemmer;
import utils.Language;
import utils.LanguageDetector;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static  Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path where documents are located:");

        //TODO Uncomment for prouction String path = scanner.nextLine();
        String path = "/home/oem/IntelliJIDEAProjects/invertedindex/src/main/resources/input";
        logger.info("Indexing process started...");

        InvertedIndex.getInstance().addFilesToIndex(path);

        System.out.println("Enter search words:");
        String searchWords;
        while (!(searchWords = scanner.nextLine()).equals("exit")) {
            List<String> result = getSearchResult(searchWords);
            if (result.isEmpty()) {
                System.out.println("> Your query doesn't match any document.");
            } else {
                System.out.println("> Your query matches the following documents, presented in a ranked order: ");
                for (String docPath : result) {
                    System.out.println("> " + docPath);
                }
            }
        }
    }

    private static List<String> getSearchResult(String searchWords) {
        List<String> result;
        if (Language.RO ==  LanguageDetector.detectLanguage(searchWords)) {
            List<String> words = new TextProcessor(searchWords, new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance())).getAllWords();
            result = InvertedIndex.getInstance().searchWordsInIndex(words);
        } else {
            List<String> words = new TextProcessor(searchWords, new WordProcessor(EnStopWordsFilter.getInstance(), EnStemmer.getInstance())).getAllWords();
            result = InvertedIndex.getInstance().searchWordsInIndex(words);
            if (result.isEmpty()) { // difficult to detect ro from few words
                words = new TextProcessor(searchWords, new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance())).getAllWords();
                result = InvertedIndex.getInstance().searchWordsInIndex(words);
            }
        }
        return result;
    }
}
