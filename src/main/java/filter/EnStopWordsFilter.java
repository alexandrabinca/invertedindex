package filter;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class EnStopWordsFilter implements IStopWordsFilter {
    private Logger logger = Logger.getLogger(EnStopWordsFilter.class);

    private Set<String> stopWords;
    private static IStopWordsFilter instance = new EnStopWordsFilter();

    public EnStopWordsFilter() {
        try {
            stopWords = loadStopWords("stopwords_en.txt");
        } catch (Exception e) {
            logger.error("Failed to load english stop words.", e);
            stopWords = new HashSet<>();
        }
    }


    public static IStopWordsFilter getInstance() {
        return instance;
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}
