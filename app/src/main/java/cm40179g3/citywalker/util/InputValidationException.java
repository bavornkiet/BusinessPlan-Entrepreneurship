package cm40179g3.citywalker.util;

public class InputValidationException extends Exception {
    public InputValidationException() {
    }

    public InputValidationException(String message) {
        super(message);
    }

    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputValidationException(Throwable cause) {
        super(cause);
    }

    public InputValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
