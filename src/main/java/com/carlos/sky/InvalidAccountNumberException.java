package com.carlos.sky;

public class InvalidAccountNumberException extends Exception{

    public InvalidAccountNumberException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }

}