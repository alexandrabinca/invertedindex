import index.InvertedIndex;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionalTest {

    @BeforeClass
    public static void  buildIndex() {
        InvertedIndex.getInstance().addFilesToIndex(new File("src/test/resources/input").getAbsolutePath());
    }

    @Test
    public void testNotFound() {
        List<String> notFound = InvertedIndex.getInstance().getSearchResult("abracadabra");
        assertEquals(0, notFound.size());
    }

    @Test
    public void testEnRoWords() {
        List<String> resultMillion = InvertedIndex.getInstance().getSearchResult("million");
        assertEquals(4, resultMillion.size());
        assertTrue(resultMillion.get(0).endsWith("Electric car.pdf"));
        assertTrue(resultMillion.get(1).endsWith("Earth.docx"));
        assertTrue(resultMillion.get(2).endsWith("Mars.pdf"));
        assertTrue(resultMillion.get(3).endsWith("Earth.txt"));

        List<String> resultMilion = InvertedIndex.getInstance().getSearchResult("milion");
        assertEquals(1, resultMilion.size());
        assertTrue(resultMilion.get(0).endsWith("BucuresÌ¦ti.pdf"));
    }

    @Test
    public void testIntersection() {
        List<String> resultMillion = InvertedIndex.getInstance().getSearchResult("million");
        assertEquals(4, resultMillion.size());
        assertTrue(resultMillion.get(0).endsWith("Electric car.pdf"));
        assertTrue(resultMillion.get(1).endsWith("Earth.docx"));
        assertTrue(resultMillion.get(2).endsWith("Mars.pdf"));
        assertTrue(resultMillion.get(3).endsWith("Earth.txt"));

        List<String> resultStars = InvertedIndex.getInstance().getSearchResult("stars");
        assertEquals(3, resultStars.size());
        assertTrue(resultStars.get(0).endsWith("Earth.docx"));
        assertTrue(resultStars.get(1).endsWith("Mars.pdf"));
        assertTrue(resultStars.get(2).endsWith("Computer science.pdf"));

        List<String> resultMillionStars = InvertedIndex.getInstance().getSearchResult("million stars");
        assertEquals(2, resultMillionStars.size());
        assertTrue(resultMillionStars.get(0).endsWith("Earth.docx"));
        assertTrue(resultMillionStars.get(1).endsWith("Mars.pdf"));
    }

    @Test
    public void testIntersectionNotFound() {
        List<String> resultStars = InvertedIndex.getInstance().getSearchResult("stars");
        assertEquals(3, resultStars.size());
        assertTrue(resultStars.get(0).endsWith("Earth.docx"));
        assertTrue(resultStars.get(1).endsWith("Mars.pdf"));
        assertTrue(resultStars.get(2).endsWith("Computer science.pdf"));

        List<String> resultMilion = InvertedIndex.getInstance().getSearchResult("milion");
        assertEquals(1, resultMilion.size());
        assertTrue(resultMilion.get(0).endsWith("BucuresÌ¦ti.pdf"));

        List<String> resultMilionStars = InvertedIndex.getInstance().getSearchResult("milion stars");
        assertEquals(0, resultMilionStars.size());
    }

    @Test
    public void testDiacritics() {
        List<String> result = InvertedIndex.getInstance().getSearchResult("București");
        assertEquals(1, result.size());
        assertTrue(result.get(0).endsWith("BucuresÌ¦ti.pdf"));
    }

    @Test
    public void testInvalidSearchWord() {
        List<String> result = InvertedIndex.getInstance().getSearchResult("!");
        assertEquals(0, result.size());
    }

    @Test
    public void testOneWordFromSearchWordsDoesNotExistInIndex() {
        List<String> resultOameni = InvertedIndex.getInstance().getSearchResult("oameni");
        assertEquals(1, resultOameni.size());
        assertTrue(resultOameni.get(0).endsWith("BucuresÌ¦ti.pdf"));

        List<String> resultMinunanti = InvertedIndex.getInstance().getSearchResult("minunati");
        assertEquals(0, resultMinunanti.size());

        List<String> resultOameniMinunanti = InvertedIndex.getInstance().getSearchResult("oameni minunati");
        assertEquals(0, resultOameniMinunanti.size());
    }
}
