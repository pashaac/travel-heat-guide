package ru.ifmo.pashaac.treii.exception;

/**
 * Created by Pavel Asadchiy
 * on 23:10 09.10.17.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
