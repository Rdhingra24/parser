package com.test.parser.demo.processor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader {

    public static List<String> readTextFile(String filePath)   {
        List<String> lines = new ArrayList<>();
        boolean isFirstLine = true;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                boolean hasEmail = hasEmailPattern(line);
                String[] parts = line.split(",");
                StringBuilder cleanedLine = new StringBuilder();
                for (String part : parts) {
                    if (!part.trim().isEmpty()) {
                        cleanedLine.append(part).append(",");
                    }
                }
                if (cleanedLine.length() > 0) {
                    cleanedLine.deleteCharAt(cleanedLine.length() - 1); // Remove the last comma
                    lines.add(cleanedLine.toString() + ", hasEmail-" + (hasEmail ? "true" : "false"));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private static boolean hasEmailPattern(String line) {
        String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

}
