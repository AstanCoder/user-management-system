package com.example.contact.application.service;

import com.example.contact.application.command.TagResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.AssignTagsUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Tag;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.TagRepository;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.TagId;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

public class AssignTagsService implements AssignTagsUseCase {

    private final ContactRepository contactRepository;
    private final TagRepository tagRepository;
    private final ContactApplicationMapper mapper;

    public AssignTagsService(
            ContactRepository contactRepository, TagRepository tagRepository, ContactApplicationMapper mapper) {
        this.contactRepository = contactRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public List<TagResult> execute(ContactId contactId, List<String> tagNames) {
        if (contactRepository.findById(contactId).isEmpty()) {
            throw new ContactNotFoundException(contactId);
        }
        tagRepository.clearContactTags(contactId);
        List<TagResult> results = new ArrayList<>();
        if (tagNames == null) {
            return results;
        }
        Set<String> uniqueTagNames = new LinkedHashSet<>();
        for (String name : tagNames) {
            if (name == null || name.isBlank()) {
                continue;
            }
            uniqueTagNames.add(name.trim());
        }
        for (String name : uniqueTagNames) {
            Tag tag = tagRepository
                    .findByName(name)
                    .orElseGet(() -> tagRepository.save(Tag.create(TagId.generate(), name)));
            tagRepository.assignToContact(contactId, tag.id());
            results.add(mapper.toTagResult(tag));
        }
        return results;
    }
}
