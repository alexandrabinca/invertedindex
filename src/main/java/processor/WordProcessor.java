package processor;


import org.apache.commons.lang3.StringUtils;
import utils.*;

public class WordProcessor {

    private IStopWordsFilter stopWordsFilter;
    private IStemmer stemmer;

    public WordProcessor(IStopWordsFilter stopWordsFilter, IStemmer stemmer) {
        this.stopWordsFilter = stopWordsFilter;
        this.stemmer = stemmer;
    }

    public String processWord(String word) {
        word = Normalizer.normalize(word);
        if (stopWordsFilter.isStopWord(word) || StringUtils.isBlank(word)) {
            return "";
        }
        return stemmer.stem(word);
    }
}
