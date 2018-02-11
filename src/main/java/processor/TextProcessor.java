package processor;

import org.apache.commons.lang3.StringUtils;
import utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TextProcessor {

    private WordProcessor wordProcessor;
    private StringTokenizer tokenizer;

    public TextProcessor(String content) {
        if (Language.EN ==  LanguageDetector.detectLanguage(content)) {
            wordProcessor = new WordProcessor(EnStopWordsFilter.getInstance(), EnStemmer.getInstance());
        } else {
            wordProcessor = new WordProcessor(RoStopWordsFilter.getInstance(), RoStemmer.getInstance());
        }
        tokenizer = new StringTokenizer(content, " -");
    }

    public TextProcessor(String content, WordProcessor wordProcessor) {
        this.wordProcessor = wordProcessor;
        tokenizer = new StringTokenizer(content, " -");

    }

    public String getNextWord() {
        if (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            String processedWord = wordProcessor.processWord(word);
            if (StringUtils.isBlank(processedWord)) {
                return getNextWord();
            } else {
                return processedWord;
            }
        } else {
            return null;
        }
    }

    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken().toLowerCase();
            String processedWord = wordProcessor.processWord(word);
            if (StringUtils.isBlank(processedWord) || processedWord.length() == 1) {
                continue;
            } else {
                words.add(processedWord);
            }
        }
        return words;
    }
}
