package com.example.contact.application.service;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.domain.port.ActivityRepository;
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
}
