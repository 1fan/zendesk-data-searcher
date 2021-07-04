package com.zendesk.datasearcher.exception;

/**
 * This exception should be thrown when failed to read the value of a field from the object.
 * Possible reason could be -  the field is not made accessible, etc.
 */
public class InvalidFieldException extends Exception {

    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(String message, Throwable cause) {
        super(message, cause);
    }

}
