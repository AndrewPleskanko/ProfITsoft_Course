package org.example.exception;

import java.io.Serial;

public class InvalidDirectoryException extends IllegalArgumentException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidDirectoryException(String message) {
        super(message);
    }
}
