package com.test.parser.demo.utils;

import com.test.parser.demo.processor.CsvFileProcessor;
import com.test.parser.demo.processor.Processor;
import com.test.parser.demo.processor.TextFileProcessor;
import org.apache.commons.validator.routines.EmailValidator;

public class Helper {
    static EmailValidator emailValidator = EmailValidator.getInstance();

    public static boolean isValidFileType(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(lastDotIndex + 1);
        return (lastDotIndex != -1) && "txt".equalsIgnoreCase(extension) || "csv".equalsIgnoreCase(extension) ;
    }

    public static Processor getProcessor(String fileId){
        return fileId.endsWith("txt") ? new TextFileProcessor() : new CsvFileProcessor();
    }

    public static boolean isValidEmail(String line ) {
            return emailValidator.isValid(line);
    }

}
