package com.example.contact.domain.valueobject;

import com.example.contact.domain.exception.InvalidContactDataException;
import java.util.regex.Pattern;

/**
 * Optional phone number normalized to digits and leading plus sign.
 */
public final class PhoneNumber {

    private static final Pattern ALLOWED = Pattern.compile("^[+0-9]{7,30}$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    /**
     * Creates a phone number from raw input.
     *
     * @param raw user input; blank yields empty optional
     * @return phone number or null when absent
     * @throws InvalidContactDataException when format is invalid
     */
    public static PhoneNumber createOptional(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String normalized = raw.trim().replaceAll("[\\s-]", "");
        if (!ALLOWED.matcher(normalized).matches()) {
            throw new InvalidContactDataException("Invalid phone number format");
        }
        return new PhoneNumber(normalized);
    }

    /**
     * Rehydrates from persistence.
     *
     * @param stored stored phone
     * @return phone instance
     */
    public static PhoneNumber fromStored(String stored) {
        return new PhoneNumber(stored);
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
