package com.example.resourceserverjwtauth.exception;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
