package com.project.spring.the_file_store.controllers;

import com.project.spring.the_file_store.Constants;
import com.project.spring.the_file_store.services.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/filestore")
public class FileStore {
    private static final Map<String, String> fileHashes = new ConcurrentHashMap<>();
    @Autowired
    FileStoreService fileStoreService;

    @PostMapping("/add")
    public ResponseEntity<String> addFile(@RequestParam String fileName, @RequestParam String hash, @RequestBody byte[] content)  {
        File file = new File(Constants.FILE_STORE_PATH + fileName);

        if (file.exists() && fileHashes.getOrDefault(fileName, "").equals(hash)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists with the same content.");
        }

        fileStoreService.addFile(file, fileName, fileHashes, hash, content);

        return ResponseEntity.status(HttpStatus.CREATED).body("File added successfully.");
    }
}
