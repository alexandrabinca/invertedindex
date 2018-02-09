package processor;

public class Normalizer {
    public static String normalize(String word) {
        word = word.replaceAll("\\W", "")
                    .replaceAll("\\d", "");

        return word;
    }
}
