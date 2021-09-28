package com.reeco.exception;

public class ReecoException extends RuntimeException {

    private static final long serialVersionUID = 601995650578985281L;

    public ReecoException(String message) {
        super(message);
    }

    public ReecoException(String message, Throwable cause) {
        super(message, cause);
    }
}