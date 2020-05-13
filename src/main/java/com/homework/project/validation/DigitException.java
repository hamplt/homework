package com.homework.project.validation;

public class DigitException extends Exception {
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public DigitException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public DigitException() {
        super();
    }
}
