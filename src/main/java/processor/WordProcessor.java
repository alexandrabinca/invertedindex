package processor;


public class WordProcessor {

    public String processWord(String word) {
        word = word.toLowerCase();
        if (StopWordsFilter.isStopWord(word)) {
            return null;
        }
        word = Normalizer.normalize(word);
        return EnglishStemmer.stem(word);
    }
}
