import index.InvertedIndex;
import processor.TextProcessor;
import processor.WordProcessor;
import utils.EnStemmer;
import utils.EnStopWordsFilter;
import utils.RoStemmer;
import utils.RoStopWordsFilter;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path where documents are located:");

        //TODO Uncomment for production String path = scanner.nextLine();
        String path = "/home/oem/IntelliJIDEAProjects/invertedindex/src/main/resources/input";
        System.out.println("> Indexing process started...");

        InvertedIndex.getInstance().addFilesToIndex(path);

        System.out.println("Enter search words:");
        String searchWords;
        while (!(searchWords = "oameni"//TODO Uncomment for production scanner.nextLine()
                 ).equals("exit")) {
            String result;
            List<String> words = new TextProcessor(searchWords, new WordProcessor(EnStopWordsFilter.getInstance(), EnStemmer.getInstance())).getAllWords();
            result = InvertedIndex.getInstance().searchWordsInIndex(words);
            if (result.equals(InvertedIndex.NO_RESULT)) {
                words = new TextProcessor(searchWords, new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance())).getAllWords();
                result = InvertedIndex.getInstance().searchWordsInIndex(words);
            }
            System.out.println(result);
            //TODO DELETE THIS LINE for production
            System.exit(0);
        }
    }
}
