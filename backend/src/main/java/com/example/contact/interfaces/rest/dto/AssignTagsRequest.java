package com.example.contact.interfaces.rest.dto;

import java.util.List;
import lombok.Value;

@Value
public class AssignTagsRequest {

    List<String> tagNames;
}
