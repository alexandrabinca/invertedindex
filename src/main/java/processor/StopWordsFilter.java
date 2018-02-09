package processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopWordsFilter {
    private static Set<String> stopWords;

    static {
        stopWords = loadStopWords("stopwords_en.txt");
    }

    private static Set<String> loadStopWords(String stopWordsFileName) {
        Set<String> set = new HashSet<String>();
        ClassLoader classLoader = StopWordsFilter.class.getClassLoader();
        File file = new File(classLoader.getResource(stopWordsFileName).getFile());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return set;
        }
        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            if (word.startsWith("#")) {
                continue;
            }
            set.add(word);
        }
        return set;
    }

    public static boolean isStopWord(String stopWord) {
        return stopWords.contains(stopWord);
    }
}
