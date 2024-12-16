package com.project.client;

public class Constants {
    public static String SERVER_URL =  System.getenv("SERVER_URL") == null ? "http://localhost:8080/filestore"
            : System.getenv("SERVER_URL");
}