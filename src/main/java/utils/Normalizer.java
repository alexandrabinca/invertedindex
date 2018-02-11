package utils;

import org.apache.commons.validator.routines.UrlValidator;

public class Normalizer {

    public static String normalize(String word) {
        if (isUrl(word) || isFilePath(word)) {
            System.out.println(word);
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

    private static boolean isUrl(String word) {
        return UrlValidator.getInstance().isValid(word);
    }

    private static boolean isFilePath(String word) {
        return word.endsWith(".jpg") || word.endsWith(".png") || word.endsWith(".svg") || word.endsWith(".html");
    }

}
