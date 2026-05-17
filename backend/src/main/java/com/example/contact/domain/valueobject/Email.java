package com.example.contact.domain.valueobject;

import com.example.contact.domain.exception.InvalidContactDataException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Email address value object with normalization to lowercase trimmed form.
 */
public final class Email {

    private static final Pattern FORMAT =
            Pattern.compile("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    /**
     * Creates an email from raw input after trim and lowercase normalization.
     *
     * @param raw user input
     * @return normalized email
     * @throws InvalidContactDataException when blank or malformed
     */
    public static Email create(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new InvalidContactDataException("Email must not be blank");
        }
        String normalized = raw.trim().toLowerCase(Locale.ROOT);
        if (!FORMAT.matcher(normalized).matches()) {
            throw new InvalidContactDataException("Invalid email format");
        }
        return new Email(normalized);
    }

    /**
     * Rehydrates an email from persistence without re-validating format.
     *
     * @param stored stored value
     * @return email instance
     */
    public static Email fromStored(String stored) {
        return new Email(stored);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email email)) {
            return false;
        }
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
