package cm40179g3.citywalker.util;

import android.util.Patterns;

public final class Validation {

    // Checks used by both LoginActivity and NewAccountActivity

    private Validation() {}

    public static void validateEmail(String email) throws InputValidationException {
        presenceCheck(email, "Email address");

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw new InputValidationException("Invalid email address format");
        }
    }

    public static void validatePassword(String password) throws InputValidationException {
        final int minLength = 6;
        presenceCheck(password, "Password");

        if (password.length() < minLength) {
            throw new InputValidationException("Password contains fewer than " + minLength + " characters");
        }

        // Could add some more validation checks here
    }

    public static void presenceCheck(String text, String fieldName) throws InputValidationException {
        if (text.isEmpty()) {
            throw new InputValidationException(fieldName + " is empty");
        }
    }

}
