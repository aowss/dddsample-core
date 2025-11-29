package se.citerus.dddsample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Utils {
    public static Stream<String[]> load(String fileName) throws IOException {
        String path = "src/test/resources/" + fileName;
        return Files
                .lines(new File(path).toPath())
                .skip(1)
                .map(line -> line.split(","));
    }
}
