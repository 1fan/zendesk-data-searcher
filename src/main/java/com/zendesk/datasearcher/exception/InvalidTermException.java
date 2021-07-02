package com.zendesk.datasearcher.exception;

//This exception should be thrown when the search term is invalid
public class InvalidTermException extends Exception{
    public InvalidTermException(String message) {
        super(message);
    }

    public InvalidTermException(String message, Throwable cause) {
        super(message, cause);
    }
}
