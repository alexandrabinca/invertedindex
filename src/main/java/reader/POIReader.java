package reader;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

import java.io.FileInputStream;
import java.io.IOException;

public class POIReader extends AbstractReader {

    public POIReader(String filePath) {
        super(filePath);
    }

    @Override
    String read() throws IOException {
        FileInputStream fis = new FileInputStream(getFilePath());
        XWPFDocument fileSystem = new XWPFDocument(fis);
        XWPFWordExtractor wordExtractor = new XWPFWordExtractor(fileSystem);
        return wordExtractor.getText();
    }
}
