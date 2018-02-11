package reader;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PDFReader extends AbstractReader {

    public PDFReader(String filePath) {
        super(filePath);
    }

    @Override
    String read() throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler(-1);
        FileInputStream fis = new FileInputStream(new File(getFilePath()));
        PDFParser parser = new PDFParser();
        parser.parse(fis, handler, new Metadata(), new ParseContext());

        return handler.toString();
    }

}
