package reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TXTReader extends AbstractReader {

    public TXTReader(String filePath) {
        super(filePath);
    }

    @Override
    String read() throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(getFilePath()));
        return new String(bytes);
    }
}
