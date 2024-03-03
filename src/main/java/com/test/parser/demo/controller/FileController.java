package com.test.parser.demo.controller;

import com.test.parser.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.test.parser.demo.utils.Helper.isValidFileType;


@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/api/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty() || !isValidFileType(file.getOriginalFilename())){
            return ResponseEntity.badRequest().body("Invalid File type. Only csv/txt is allowed [{}] " + file.getOriginalFilename());
        }
        String fileId = fileService.storeFile(file);
        fileService.processFileInAsyncMode(fileId);
        return ResponseEntity.ok().body("File upload received. Processing is in progress with ID: " + fileId);
    }

    @GetMapping("/api/download/{fileName:.+}")
    public ResponseEntity<?> download(@PathVariable String fileName) {
        return fileService.downloadFile(fileName);
    }
}
