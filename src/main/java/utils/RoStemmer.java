package utils;

import org.apache.commons.lang3.StringUtils;
import org.tartarus.snowball.ext.RomanianStemmer;

public class RoStemmer implements IStemmer {

    private static RoStemmer instance = new RoStemmer();

    private RoStemmer() { }

    public static IStemmer getInstance() {
        return instance;
    }

    public String stem(String word) {
        if (StringUtils.isEmpty(word)) {
            return word;
        }
        RomanianStemmer stemmer = new RomanianStemmer();
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
