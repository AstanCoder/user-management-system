package com.example.contact.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Standard API error response body.
 */
@Schema(name = "ErrorResponse", description = "Error payload returned for failed requests")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP error label", example = "Bad Request")
    private String error;

    @Schema(description = "Error message", example = "Validation failed")
    private String message;

    @Schema(description = "Request path", example = "/api/contacts")
    private String path;

    @Schema(description = "Field validation errors when applicable")
    private List<FieldError> errors;

    /**
     * Field-level validation error entry.
     */
    @Schema(name = "FieldError")
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {

        @Schema(description = "Field name", example = "email")
        private String field;

        @Schema(description = "Validation message", example = "must be a well-formed email address")
        private String message;
    }
}
