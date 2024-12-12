package com.project.client.services;

import com.project.client.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateFileService {
    public void updateFile(HttpClient client, String fileName) throws IOException, InterruptedException {
            Path path = Paths.get("client/fileupload/" + fileName);
            if (!Files.exists(path)) {
                System.out.println("File does not exist: " + fileName);
                return;
            }

            String hash = String.valueOf(Files.mismatch(path, path));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Constants.SERVER_URL + "/update?fileName=" +fileName + "&hash=" + hash))
                    .PUT(HttpRequest.BodyPublishers.ofFile(path))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
    }
}
