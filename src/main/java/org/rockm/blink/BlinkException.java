package org.rockm.blink;

public class BlinkException extends RuntimeException {
    public BlinkException() {
    }

    public BlinkException(String message) {
        super(message);
    }

    public BlinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlinkException(Throwable cause) {
        super(cause);
    }

    public BlinkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
