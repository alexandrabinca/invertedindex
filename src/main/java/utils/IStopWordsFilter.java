package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public interface IStopWordsFilter {
    boolean isStopWord(String word);

    default Set<String> loadStopWords(String stopWordsFileName) throws FileNotFoundException {
        Set<String> set = new HashSet<>();
        ClassLoader classLoader = IStopWordsFilter.class.getClassLoader();
        File file = new File(classLoader.getResource(stopWordsFileName).getFile());
        Scanner scanner  = new Scanner(file);
        while (scanner.hasNext()) {
            String word = scanner.nextLine();
            if (word.startsWith("#") || word.isEmpty()) {
                continue;
            }
            set.add(word);
        }
        return set;
    }
}
