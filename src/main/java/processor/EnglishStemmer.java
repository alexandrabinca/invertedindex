package processor;

import org.tartarus.martin.Stemmer;

public class EnglishStemmer {

    public static String stem(String word) {
        if (word == null) {
            return null;
        }
        Stemmer stemmer = new Stemmer();
        for (Character c : word.toCharArray()) {
            stemmer.add(c);
        }
        stemmer.stem();
        return stemmer.toString();
    }
}
