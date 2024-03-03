package com.test.parser.demo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "root")
@ConfigurationPropertiesScan
@Data
public class Configuration {
    private String fileUploadDir;
    private String fileDownloadDir;

}
