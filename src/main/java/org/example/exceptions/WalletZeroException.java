package org.example.exceptions;

public class WalletZeroException extends RuntimeException {
    public WalletZeroException(final String message) {
        super(message);
    }
}
