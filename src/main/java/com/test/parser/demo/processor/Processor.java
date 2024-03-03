package com.test.parser.demo.processor;

import org.springframework.stereotype.Component;

@Component
public interface Processor {
    void process(String file,String inputDirectory,String outputDirectory);
}
