package com.reeco.transport.utils.exception;


public class FileProcessingException extends RuntimeException{
    public FileProcessingException(){}

    public FileProcessingException(String message){ super(message);}

    public FileProcessingException(String message, Throwable cause){super(message,cause);}

    public FileProcessingException(Throwable cause){super(cause);}
}
