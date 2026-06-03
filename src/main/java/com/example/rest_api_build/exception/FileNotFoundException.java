package com.example.rest_api_build.exception;

public class FileNotFoundException
        extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }
}