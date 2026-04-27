package com.neo.digital_banking_api.exception;

/**
 * Custom exception for Digital Banking API
 */
public class DigitalBankingException extends RuntimeException {

    public DigitalBankingException(String message) {
        super(message);
    }

    public DigitalBankingException(String message, Throwable cause) {
        super(message, cause);
    }
}

