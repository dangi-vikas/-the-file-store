package com.project.spring.the_file_store.controllers;

import com.project.spring.the_file_store.services.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.project.spring.the_file_store.Constants.FILE_STORE_PATH;

@RestController
@RequestMapping("/filestore")
public class FileStore {
    private static final Map<String, String> fileHashes = new ConcurrentHashMap<>();
    @Autowired
    FileStoreService fileStoreService;

    @PostMapping("/add")
    public ResponseEntity<String> addFile(@RequestParam String fileName, @RequestParam String hash, @RequestBody byte[] content)  {
        File file = new File(FILE_STORE_PATH + fileName);

        if (file.exists() && fileHashes.getOrDefault(fileName, "").equals(hash)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists with the same content.");
        }

        fileStoreService.addFile(file, fileName, fileHashes, hash, content);

        return ResponseEntity.status(HttpStatus.CREATED).body("File added successfully.");
    }

    @GetMapping("/list")
    public ResponseEntity<String> listFiles() {
        String[] files = fileStoreService.listFiles();

        if (files == null || files.length == 0) {
            return ResponseEntity.ok("No files in the store.");
        }

        return ResponseEntity.ok(String.join("\n", files));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFile(@RequestParam String fileName) {
        File file = new File(FILE_STORE_PATH + fileName);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        fileStoreService.removeFile(fileName, file, fileHashes);
        return ResponseEntity.ok("File removed successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateFile(@RequestParam String fileName, @RequestParam String hash, @RequestBody byte[] content) throws IOException {
        File file = new File(FILE_STORE_PATH + fileName);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        }

        if (file.exists() && fileHashes.getOrDefault(fileName, "").equals(hash)) {
            return ResponseEntity.ok("File already has the same content.");
        }

        fileStoreService.updateFile(file, content, hash, fileHashes, fileName);

        return ResponseEntity.ok("File updated successfully.");
    }

    @GetMapping("/wc")
    public ResponseEntity<String> wordCount() throws IOException {
        long wordCount = fileStoreService.getWordCount();

        return ResponseEntity.ok("Total words: " + wordCount);
    }

    @GetMapping("/freq-words")
    public ResponseEntity<String> frequentWords(@RequestParam int limit, @RequestParam(defaultValue = "dsc") String order) throws IOException {
        boolean ascending = "asc".equals(order);
        String result = fileStoreService.findFrequentWords(limit, ascending);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkHash")
    public ResponseEntity<String> checkHash(@RequestParam String hash) {
        if (fileHashes.containsValue(hash)) {
            return ResponseEntity.ok("DUPLICATE");
        }
        return ResponseEntity.ok("UNIQUE");
    }
}
