package utils;

import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.EnglishStemmer;

public class EnStemmer implements IStemmer {

    private static EnStemmer instance = new EnStemmer();

    private EnStemmer() {

    }

    public static IStemmer getInstance() {
        return instance;
    }

    public String stem(String word) {
        if (StringUtils.isEmpty(word)) {
            return word;
        }
        EnglishStemmer stemmer = new EnglishStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
