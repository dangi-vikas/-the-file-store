package com.project.client;

import com.project.client.services.AddFileService;

import java.io.IOException;
import java.net.http.HttpClient;

public class ProcessCommand {

    public void processCommand(String command) {
        HttpClient client = HttpClient.newHttpClient();
        String[] parts = command.split(" ");
        if(parts.length < 2) return;
        String action = parts[1];

        switch (action) {
            case "add":
                if (parts.length < 2) {
                    System.out.println("Usage: add <file1> <file2> ...");
                    return;
                }
                for (int i = 2; i < parts.length; i++) {
                    try {
                        new AddFileService().addFile(client, parts[i]);
                    } catch (IOException | InterruptedException e) {
                        System.out.print("Some unknown error occurred!");
                    }
                }
                break;

            case "ls":
                break;

            case "rm":
                if (parts.length != 2) {
                    System.out.println("Usage: rm <file>");
                    return;
                }
                break;

            case "update":
                if (parts.length != 2) {
                    System.out.println("Usage: update <file>");
                    return;
                }
                break;

            case "wc":
                break;

            case "freq-words":
                break;

            default:
                System.out.println("Unknown command. Supported commands: add, ls, rm, update, wc, freq-words");
        }
    }
}
