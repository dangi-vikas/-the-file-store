package com.project.client;

import com.project.client.services.*;

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
                if (parts.length < 3) {
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
                try {
                    new ListFileService().listFiles(client);
                } catch (IOException | InterruptedException e) {
                    System.out.print("Some unknown error occurred!");
                }
                break;

            case "rm":
                if (parts.length != 3) {
                    System.out.println("Usage: rm <file>");
                    return;
                }
                try {
                    RemoveFileService.removeFile(client, parts[2]);
                } catch (IOException | InterruptedException e) {
                    System.out.print("Some unknown error occurred!");
                }
                break;

            case "update":
                if (parts.length != 3) {
                    System.out.println("Usage: update <file>");
                    return;
                }
                try {
                    new UpdateFileService().updateFile(client, parts[2]);
                } catch (IOException | InterruptedException e) {
                    System.out.print("Some unknown error occurred!");
                }
                break;

            case "wc":
                try {
                    new WordCountService().getWordCount(client);
                } catch (IOException | InterruptedException e) {
                    System.out.print("Some unknown error occurred!");
                }
                break;

            case "freq-words":
                int limit = 10;
                String order = "dsc";
                for (int i = 2; i < parts.length; i++) {
                    if (parts[i].startsWith("--limit=")) {
                        limit = Integer.parseInt(parts[i].substring("--limit=".length()));
                    } else if (parts[i].startsWith("--order=")) {
                        order = parts[i].substring("--order=".length());
                    }
                }
                try {
                    new FindFrequentWordService().frequentWords(client, limit, order);
                } catch (IOException | InterruptedException e) {
                    System.out.print("Some unknown error occurred!");
                }
                break;

            default:
                System.out.println("Unknown command. Supported commands: add, ls, rm, update, wc, freq-words");
        }
    }
}
