package reader;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.IOException;

abstract class AbstractReader {
    private String filePath;

    protected AbstractReader(String path) {
        this.filePath = path;
    }

    abstract String read() throws IOException, TikaException, SAXException;

    public String getFilePath() {
        return filePath;
    }
}
