package com.project.spring.the_file_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class TheFileStoreApplication {

	public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(Constants.FILE_STORE_PATH));
        } catch (IOException e) {
           System.out.println("Some error occurred");
        }

        SpringApplication.run(TheFileStoreApplication.class, args);
	}

}
