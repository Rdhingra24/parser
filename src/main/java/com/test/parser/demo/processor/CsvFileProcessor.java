package com.test.parser.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CsvFileProcessor implements Processor {
    @Override
    public void process(String file,String inputDirectory,String outputDirectory) {
        log.info("Parse csv file: [{}]",file);
        CsvFileReader reader = new CsvFileReader();
        List<String[]> lines = reader.readCsv(inputDirectory+file);
        CsvFileWriter writer = new CsvFileWriter(outputDirectory + file);
        writer.writeCSV(lines);
    }
}