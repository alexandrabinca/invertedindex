package index;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;
import processor.TextProcessor;
import reader.DocumentReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class FileProcessingTask implements Callable<Map<String, Integer>> {

    private File file;

    public FileProcessingTask(File file) {
        this.file = file;
    }

    public Map<String, Integer> call() throws IOException, TikaException, SAXException {
        Map<String, Integer> wordCounter = new ConcurrentHashMap<>();
        DocumentReader reader = new DocumentReader(file);
        TextProcessor textProcessor = new TextProcessor(reader.readContent());
        String processedWord;
        while ((processedWord = textProcessor.getNextWord()) != null) {
            if (!wordCounter.containsKey(processedWord)) {
                wordCounter.put(processedWord, 0);
            }
            wordCounter.put(processedWord, wordCounter.get(processedWord) + 1);
        }
        return wordCounter;
    }
}
