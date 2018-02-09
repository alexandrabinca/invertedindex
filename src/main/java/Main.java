import processor.TxtFileProcessor;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path where documents are located:");

        String path = scanner.nextLine();
        File file = new File(path);
        System.out.println("> Indexing process started...");

        processFile(file);

        System.out.println("Enter search words:");
        String searchWords;
        while (!(searchWords = scanner.nextLine()).equals("exit")) {

        }
        executorService.shutdown();
    }

    private static void processFile(File file) throws ExecutionException, InterruptedException {
        if (!file.exists()) {
            System.exit(-1);
        }
        if (file.isDirectory()) {
            for (File f :  file.listFiles()) {
                processFile(f);
            }
        } else {
            Future<Map<String, Integer>> documentWordCount = executorService.submit(new TxtFileProcessor(file));
            System.out.println(documentWordCount.get());
        }
    }
}
