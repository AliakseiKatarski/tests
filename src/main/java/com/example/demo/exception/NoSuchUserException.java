package com.example.demo.exception;

public class NoSuchUserException extends RuntimeException {

    private static final String MESSAGE = "No such user";

    public NoSuchUserException() {
        super(MESSAGE);
    }

}
