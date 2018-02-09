package processor;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class TxtFileProcessor extends WordProcessor implements Callable<Map<String, Integer>> {

    private File file;
    private Map<String, Integer> wordCount =  new ConcurrentHashMap<String, Integer>();

    public TxtFileProcessor(File file) {
        this.file = file;
    }

    public Map<String, Integer> call() throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            String word = scanner.next();
            String processedWord = processWord(word);
            if (StringUtils.isEmpty(processedWord)) {
                continue;
            }
            if (!wordCount.containsKey(processedWord)) {
                wordCount.put(processedWord, 0);
            }
            wordCount.put(processedWord, wordCount.get(processedWord) + 1);
        }
        return wordCount;
    }
}
