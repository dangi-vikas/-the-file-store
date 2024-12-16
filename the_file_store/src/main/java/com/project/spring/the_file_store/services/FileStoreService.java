package com.project.spring.the_file_store.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.project.spring.the_file_store.Constants.FILE_STORE_PATH;

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

    public String[] listFiles() {
        File folder = new File(FILE_STORE_PATH);
        String[] files = folder.list();
        return  files;
    }

    public void removeFile(String fileName, File file, Map<String, String> fileHashes) {
        file.delete();
        fileHashes.remove(fileName);
    }

    public void updateFile(File file, byte[] content, String hash, Map<String, String> fileHashes, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content);
        } catch (IOException e) {
            System.out.println("Some error occurred");
        }

        fileHashes.put(fileName, hash);
    }


    public long getWordCount() {
        long wordCount = 0;
        try {
            wordCount = Files.walk(Paths.get(FILE_STORE_PATH))
                .filter(Files::isRegularFile)
                .mapToLong(path -> {
                    try {
                        return Files.lines(path).flatMap(line -> Arrays.stream(line.split("\\s+"))).count();
                    } catch (IOException e) {
                        return 0;
                    }
                }).sum();
        } catch (IOException e) {
            System.out.println("Some error occurred");
        }

        return wordCount;
    }

    public String findFrequentWords(int limit, boolean ascending) {
        Map<String, Long> wordFrequency = null;
        try {
            wordFrequency = Files.walk(Paths.get(FILE_STORE_PATH))
                    .filter(Files::isRegularFile)
                    .flatMap(path -> {
                        try {
                            return Files.lines(path).flatMap(line -> Arrays.stream(line.split("\\s+")));
                        } catch (IOException e) {
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        } catch (IOException e) {
            System.out.println("Some error occurred");
        }

        String result = wordFrequency.entrySet().stream()
                .sorted((a, b) -> ascending ? a.getValue().compareTo(b.getValue()) : b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        return result;
    }

    public void addFileWhenAlreadyDuplicateFileAvailable(String hash, String newFileName, Map<String, String> fileHashes) {
        String oldFileName = fileHashes.entrySet().stream()
                .filter(hashes -> hashes.getValue().equals(hash))
                .map(Map.Entry::getKey)
                .findFirst().orElse("");

        if(oldFileName.equalsIgnoreCase(newFileName)) return;

        File originalFile = new File(FILE_STORE_PATH + oldFileName);
        Path newFile = Paths.get(FILE_STORE_PATH + newFileName);
        try {
            Files.copy(originalFile.toPath(), newFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Some error occurred");
        }
    }
}
