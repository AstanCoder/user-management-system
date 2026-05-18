package com.example.contact.application.service;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactSearchQuery;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.query.ContactSearchCriteria;
import java.util.List;

/**
 * Lists contacts with pagination from the repository port.
 */
public final class ListContactsService implements ListContactsUseCase {

    private final ContactRepository contactRepository;
    private final ContactApplicationMapper contactApplicationMapper;

    public ListContactsService(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        this.contactRepository = contactRepository;
        this.contactApplicationMapper = contactApplicationMapper;
    }

    @Override
    public ContactPageResult execute(ContactSearchQuery query) {
        ContactSearchCriteria criteria = ContactSearchCriteria.builder()
                .search(query.getSearch())
                .email(query.getEmail())
                .phone(query.getPhone())
                .tagNames(query.getTagNames())
                .page(query.getPage())
                .size(query.getSize())
                .sort(query.getSort())
                .build();
        long total = contactRepository.countSearch(criteria);
        int size = Math.max(query.getSize(), 1);
        int totalPages = (int) Math.ceil((double) total / size);
        List<com.example.contact.application.command.ContactResult> content =
                contactRepository.search(criteria).stream()
                        .map(contactApplicationMapper::toResult)
                        .toList();
        return ContactPageResult.builder()
                .content(content)
                .totalElements(total)
                .totalPages(totalPages)
                .page(query.getPage())
                .size(size)
                .build();
    }
}
