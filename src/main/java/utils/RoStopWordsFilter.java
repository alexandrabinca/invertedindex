package utils;

import java.util.HashSet;
import java.util.Set;

public class RoStopWordsFilter implements IStopWordsFilter {
    private Set<String> stopWords;
    private static IStopWordsFilter instance = new RoStopWordsFilter();

    public RoStopWordsFilter() {
        try {
            stopWords = loadStopWords("stopwords_ro.txt");
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
