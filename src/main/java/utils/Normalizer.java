package utils;

public class Normalizer {
    public static String normalize(String word) {
        if (word.startsWith("http") || word.startsWith("image:") || word.contains("wikimedia")
                || word.startsWith("file:") || word.endsWith(".jpg") || word.endsWith(".png") || word.endsWith(".svg")
                || word.endsWith(".html")) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Character c : word.toCharArray()) {
            if (Character.isAlphabetic(c)) {
                sb.append(c);
            }
        }
        return  sb.toString();
    }
}
