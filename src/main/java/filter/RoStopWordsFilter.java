package filter;

import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class RoStopWordsFilter implements IStopWordsFilter {
    private Logger logger = Logger.getLogger(RoStopWordsFilter.class);

    private Set<String> stopWords;
    private static IStopWordsFilter instance = new RoStopWordsFilter();

    public RoStopWordsFilter() {
        try {
            stopWords = loadStopWords("stopwords_ro.txt");
        } catch (Exception e) {
            logger.error("Failed to load romanian stop words.", e);
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
