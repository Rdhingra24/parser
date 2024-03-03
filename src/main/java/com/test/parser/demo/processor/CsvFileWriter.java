package com.test.parser.demo.processor;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {
    private String outputFile;

    CsvFileWriter(String outputFile) {
        this.outputFile = outputFile;
    }
    public void writeCSV(List<String[]> lines) {
        File file = new File(outputFile);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFile))) {
            for (String[] line : lines) {
                writer.writeNext(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
