package com.test.parser.demo.service;

import com.test.parser.demo.configuration.Configuration;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.test.parser.demo.utils.Helper.getProcessor;

@Service
@Slf4j
public class FileService {
    @Autowired
    Configuration configuration;

    @Getter
    private String fileUploadDirectory;

    @Getter
    private String fileDownloadDirectory;
    private final ConcurrentHashMap<String, Boolean> processingFiles = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        try {
            this.fileUploadDirectory = configuration.getFileUploadDir();
            this.fileDownloadDirectory = configuration.getFileDownloadDir();
            Files.createDirectories(Paths.get(fileUploadDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String storedFileName = UUID.randomUUID() + fileExtension;
        try {
            Path targetLocation = Paths.get(configuration.getFileUploadDir() + storedFileName);
            Files.copy(file.getInputStream(), targetLocation);
            log.info("File stored at [{}]",targetLocation);
            return storedFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Async
    public void processFileInAsyncMode(String fileId) {
        processingFiles.put(fileId,true);
        getProcessor(fileId).process(fileId,configuration.getFileUploadDir(),configuration.getFileDownloadDir());
        processingFiles.put(fileId,false);
        log.info("status completed [{}]--> [{}]",fileId,processingFiles.get(fileId));

    }

    public ResponseEntity downloadFile(String fileName) {
        if(processingFiles.getOrDefault(fileName,false)){
            return ResponseEntity.status(HttpStatus.LOCKED).body("File is still processing. Please try again later.");
        }
        Path filePath = Paths.get(getFileDownloadDirectory()).resolve(fileName).normalize();
        log.info("Try to get file from [{}]",filePath);
        try {
            Resource resource = new UrlResource(filePath.toUri());
            byte[] data = Files.readAllBytes(filePath);

            if(resource.exists() && resource.isReadable()) {
                log.info("File [{}] found, all okay!!",filePath);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(data);
            } else {
                log.info("File [{}] NOT found, something is wrong!!",filePath);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no file by this ID found. Please try again.");
            }
        } catch (MalformedURLException ex) {
            log.info("OOPS!! [{}]",ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
