package com.test.parser.demo.processor;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.test.parser.demo.utils.Helper.isValidEmail;

@Component
public class CsvFileReader {

    public List<String[]>  readCsv(String csvFile){ List<String[]> updatedLines = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            boolean isFirstLine = true;

            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 0 || nextLine[0].isEmpty()) {
                    continue;
                }

                StringBuilder updatedLine = new StringBuilder();
                boolean hasValidEmail = false;

                for (int i = 0; i < nextLine.length; i++) {
                    updatedLine.append(nextLine[i]);
                    if (i < nextLine.length - 1) {
                        updatedLine.append(",");
                    } else {
                        if (isValidEmail(nextLine[i])) {
                            hasValidEmail = true;
                        }
                    }
                }
                updatedLine.append(", hasEmail-").append(hasValidEmail ? "true" : "false");

                updatedLines.add(new String[]{updatedLine.toString()});
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return updatedLines;
    }
}
