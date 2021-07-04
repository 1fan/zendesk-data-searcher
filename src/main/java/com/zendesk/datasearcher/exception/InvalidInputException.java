package com.zendesk.datasearcher.exception;

/**
 * This exception should be thrown when the user input is invalid.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
