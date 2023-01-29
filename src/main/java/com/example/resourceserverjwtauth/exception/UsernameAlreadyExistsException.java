package com.example.resourceserverjwtauth.exception;

/**
 * @author Sandaru Anjana <sandaruanjana@outlook.com>
 */
public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
