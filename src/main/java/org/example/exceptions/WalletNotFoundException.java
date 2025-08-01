package org.example.exceptions;

public class WalletNotFoundException extends RuntimeException{
    public WalletNotFoundException(final String message) {
        super(message);
    }
}


