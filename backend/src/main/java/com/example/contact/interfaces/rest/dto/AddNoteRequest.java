package com.example.contact.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class AddNoteRequest {

    @NotBlank String content;
}
