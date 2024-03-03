package com.test.parser.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TextFileProcessor implements Processor {
    @Override
    public void process(String file,String inputDirectory,String outputDirectory)   {
        log.info("Parse text file: [{}]",file);
        TextReader reader = new TextReader();
        List<String> lines = reader.readTextFile(inputDirectory+file);
        TextFileWriter writer = new TextFileWriter(outputDirectory + file);
        writer.writeText(lines);
    }
}
