package com.example.contact.application.service;

import com.example.contact.application.command.ActivityPageResult;
import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.command.ActivitySearchQuery;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.query.ActivityListCriteria;
import com.example.contact.domain.valueobject.ContactId;

public final class ListActivitiesService implements ListActivitiesUseCase {

    private final ActivityRepository activityRepository;
    private final ContactApplicationMapper mapper;

    public ListActivitiesService(ActivityRepository activityRepository, ContactApplicationMapper mapper) {
        this.activityRepository = activityRepository;
        this.mapper = mapper;
    }

    @Override
    public java.util.List<ActivityResult> execute(ContactId contactId) {
        return activityRepository.findByContactId(contactId).stream()
                .map(mapper::toActivityResult)
                .toList();
    }

    @Override
    public ActivityPageResult search(ContactId contactId, ActivitySearchQuery query) {
        var page = activityRepository.search(
                contactId,
                ActivityListCriteria.builder()
                        .activityType(query.getActivityType())
                        .page(query.getPage())
                        .size(query.getSize())
                        .build());
        return ActivityPageResult.builder()
                .content(page.getContent().stream().map(mapper::toActivityResult).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getPage())
                .size(page.getSize())
                .build();
    }
}
