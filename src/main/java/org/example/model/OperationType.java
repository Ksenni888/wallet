package org.example.model;

public enum OperationType {
    DEPOSIT,
    WITHDRAW;

    public static boolean findByName(String name) {
        boolean result = false;
        for (OperationType type : values()) {
            if (type.name().equals(name)) {
                result = true;
                break;
            }
        }
        return result;
    }
}