package com.alvicom.financialtransaction.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class IOUtil {

    private IOUtil() {
    }

    public static List<String> getFileContentAsList(String pathToFile) {
        List<String> linesInArray = new ArrayList<>();

        Path path = Paths.get(pathToFile);

        try (Stream<String> fileAsStream = Files.lines(path)) {
            fileAsStream.forEach(aLine -> {
                linesInArray.add(aLine.trim());
            });

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return linesInArray;
    }
}
