package reader;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

public class DocumentReader {
    private AbstractReader reader;

    public DocumentReader(File file) {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String filePath = file.getPath();
        switch (fileExtension.toLowerCase()) {
            case "pdf":
                reader = new PDFReader(filePath);
                break;
            case "docx":
                reader = new POIReader(filePath);
                break;
            case "txt":
                reader = new TXTReader(filePath);
                break;
            default:
                throw new RuntimeException("invalid extension");
        }
    }


    public String readContent() throws TikaException, IOException, SAXException {
        return reader.read();
    }
}
