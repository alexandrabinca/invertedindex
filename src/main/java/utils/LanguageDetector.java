package utils;

public class LanguageDetector {
    public static Language detectLanguage(String sample) {
        for (Character c : sample.toCharArray()) {
            if (c == 'Ă' || c == 'ă' || c == 'Â' || c == 'â' || c == 'Î' || c == 'î' ||
                    c == 'Ș' || c == 'ș' || c == 'Ț' || c == 'ț') {
                return Language.RO;
            }
        }
        //TODO - add more rules
        return Language.EN;
    }
}
