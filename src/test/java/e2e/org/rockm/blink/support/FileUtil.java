package e2e.org.rockm.blink.support;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    public static byte[] fileInBytes(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        return Files.readAllBytes(path);
    }

}
