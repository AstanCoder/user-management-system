package com.example.contact.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TagResponse {

    String id;
    String name;
}
