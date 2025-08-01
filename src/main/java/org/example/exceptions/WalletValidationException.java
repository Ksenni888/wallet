package org.example.exceptions;

public class WalletValidationException extends RuntimeException{
    public WalletValidationException(final String message) {
        super(message);
    }
}