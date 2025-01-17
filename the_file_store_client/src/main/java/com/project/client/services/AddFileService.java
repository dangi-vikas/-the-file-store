package com.project.client.services;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.project.client.Constants.SERVER_URL;
import static java.nio.file.Files.newBufferedWriter;

public class AddFileService {
    public void addFile(HttpClient client, String fileName) throws IOException, InterruptedException, NoSuchAlgorithmException {
        String filePath = "the_file_store_client/fileupload/" + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write("New File");
            writer.close();
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String fileHash = Base64.getEncoder().encodeToString(md.digest(fileBytes));

        HttpRequest hashCheckRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/checkHash?hash=" + fileHash + "&newFileName=" + fileName))
                .GET()
                .build();

        HttpResponse<String> response = client.send(hashCheckRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && response.body().equals("DUPLICATE")) {
            System.out.println("Duplicate content found on the server. File added with metadata only: " + fileName);
            return;
        }

        HttpRequest uploadRequest = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL + "/add?fileName=" +fileName + "&hash=" + fileHash))
                .POST(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                .build();

        HttpResponse<String> uploadResponse = client.send(uploadRequest, HttpResponse.BodyHandlers.ofString());

        if (uploadResponse.statusCode() == 201) {
            System.out.println("File added successfully: " + fileName);
        } else {
            System.out.println("Error adding file: " + uploadResponse.body());
        }
    }
}
