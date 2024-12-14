package com.project.client;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("***** Welcome to The File Store Service ***** \n\n--List of Commands-- \n\n1. Add a file: store add <file_name> \n"
        + "2. List all files on the server: store ls \n3. Remove a file: store rm <file_name> \n4. Update a file: store update <file_name> \n"
        + "5. Get the count of all the words in the files: store wc \n6: Find least or most frequent words with count: store freq-word --limit="
        + "<number_of_words> --order=<asc/desc>\n");

        if (args.length > 0) {
            new ProcessCommand().processCommand(args[0]);
        } else {
            while(true) {
                System.out.print("> ");

                Scanner scanner = new Scanner(System.in);
                String command = scanner.nextLine().trim();

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("***** Exiting The File Store Service... *****");
                    break;
                }

                try {
                    new ProcessCommand().processCommand(command);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
    }
}