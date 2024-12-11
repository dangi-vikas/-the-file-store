package com.project.spring.the_file_store.services;

import com.project.spring.the_file_store.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class FileStoreService {
    public void addFile(File file, String fileName, Map<String, String> fileHashes, String hash, byte[] content) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        } catch (IOException e) {
            System.out.println("Some error occurred");
        }

        fileHashes.put(fileName, hash);
    }
}
