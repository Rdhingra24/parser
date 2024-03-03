package com.test.parser.demo.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TextFileWriter {
    private String outputFile;

    public TextFileWriter(String outputFile) {
        this.outputFile = outputFile;
    }

    public void writeText(List<String> lines) {
        File file = new File(outputFile);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + outputFile, e);
        }
    }
}
