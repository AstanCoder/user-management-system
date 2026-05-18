package com.example.contact.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import lombok.Value;

@Value
public class LogActivityRequest {

    @NotBlank String activityType;

    @NotBlank String description;

    Instant occurredAt;

    Boolean confirmed;
}
