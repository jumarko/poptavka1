package com.eprovement.poptavka.exception;

/**
 * Represents more specific failure of login error.
 * This can be used for application purposes, but end user should never find out if it gives invalid email or password!
 */
public class LoginUserNotExistException extends LoginException {

    private static final String COMMON_MESSAGE = "User with specified email does not exist!";

    public LoginUserNotExistException() {
        super(COMMON_MESSAGE);
    }

    public LoginUserNotExistException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public LoginUserNotExistException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
