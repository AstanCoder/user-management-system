package com.example.contact.application.command;

import lombok.Builder;
import lombok.Value;

/**
 * Application result for a tag.
 */
@Value
@Builder
public class TagResult {

    String id;
    String name;
}
