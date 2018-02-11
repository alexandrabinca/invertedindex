package index;

import index.InvertedIndex;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class IndexingTask implements Callable<Void> {
    private String filePath;
    private Future<Map<String, Integer>> wordCounter;

    public IndexingTask(String filePath, Future<Map<String, Integer>> wordCounter) {
        this.filePath = filePath;
        this.wordCounter = wordCounter;
    }

    @Override
    public Void call() throws Exception {
        InvertedIndex.getInstance().addWordsToIndex(filePath, wordCounter.get());
        return null;
    }
}
