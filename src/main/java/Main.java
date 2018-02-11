import index.InvertedIndex;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static  Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path where documents are located:");

        String path = scanner.nextLine();
        logger.info("Indexing process started...");

        InvertedIndex.getInstance().addFilesToIndex(path);

        System.out.println("Enter search words:");
        String searchWords;
        while (!(searchWords = scanner.nextLine()).equals("exit")) {
            List<String> result = InvertedIndex.getInstance().getSearchResult(searchWords);
            if (result.isEmpty()) {
                System.out.println("> Your query doesn't match any document.");
            } else {
                System.out.println("> Your query matches the following documents, presented in a ranked order: ");
                for (String docPath : result) {
                    System.out.println("> " + docPath);
                }
            }
        }
    }

}
