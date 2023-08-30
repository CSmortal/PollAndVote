package com.joven.poller.exception;

public class InvalidPollDataException extends Exception {
    private static final String prefix = "Invalid Poll data";
    public InvalidPollDataException() {
        super(prefix);
    }

    public InvalidPollDataException(String message) {
        super(prefix + ": " + message);
    }

}
