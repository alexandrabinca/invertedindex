package utils;

import java.util.HashSet;
import java.util.Set;

public class EnStopWordsFilter implements IStopWordsFilter {
    private Set<String> stopWords;
    private static IStopWordsFilter instance = new EnStopWordsFilter();

    public EnStopWordsFilter() {
        try {
            stopWords = loadStopWords("stopwords_en.txt");
        } catch (Exception e) {
            // TODO log error
            stopWords = new HashSet<>();
        }
    }


    public static IStopWordsFilter getInstance() {
        return instance;
    }

    public boolean isStopWord(String stopWord) {
        return stopWords.contains(stopWord);
    }
}
