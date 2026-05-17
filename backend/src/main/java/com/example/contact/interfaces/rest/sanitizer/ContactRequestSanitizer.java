package com.example.contact.interfaces.rest.sanitizer;

import com.example.contact.interfaces.rest.dto.CreateContactRequest;
import com.example.contact.interfaces.rest.dto.UpdateContactRequest;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;

/**
 * Sanitizes REST request DTO string fields before validation and mapping.
 */
public final class ContactRequestSanitizer {

    private ContactRequestSanitizer() {}

    /**
     * Returns a sanitized copy of a create request.
     *
     * @param request original request
     * @return sanitized request
     */
    public static CreateContactRequest sanitize(CreateContactRequest request) {
        return CreateContactRequest.builder()
                .firstName(sanitizeName(request.getFirstName()))
                .lastName(sanitizeName(request.getLastName()))
                .email(sanitizeEmail(request.getEmail()))
                .phone(sanitizePhone(request.getPhone()))
                .build();
    }

    /**
     * Returns a sanitized copy of an update request.
     *
     * @param request original request
     * @return sanitized request
     */
    public static UpdateContactRequest sanitize(UpdateContactRequest request) {
        return UpdateContactRequest.builder()
                .firstName(sanitizeName(request.getFirstName()))
                .lastName(sanitizeName(request.getLastName()))
                .email(sanitizeEmail(request.getEmail()))
                .phone(sanitizePhone(request.getPhone()))
                .build();
    }

    private static String sanitizeName(String value) {
        if (value == null) {
            return null;
        }
        return StringUtils.normalizeSpace(value.trim());
    }

    private static String sanitizeEmail(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private static String sanitizePhone(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().replaceAll("[\\s-]", "");
    }
}
