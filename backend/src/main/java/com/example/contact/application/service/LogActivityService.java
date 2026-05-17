package com.example.contact.application.service;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.LogActivityUseCase;
import com.example.contact.domain.exception.ContactNotFoundException;
import com.example.contact.domain.model.Activity;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;
import java.time.Instant;
import java.util.UUID;

public final class LogActivityService implements LogActivityUseCase {

    private final ContactRepository contactRepository;
    private final ActivityRepository activityRepository;
    private final ContactApplicationMapper mapper;

    public LogActivityService(
            ContactRepository contactRepository,
            ActivityRepository activityRepository,
            ContactApplicationMapper mapper) {
        this.contactRepository = contactRepository;
        this.activityRepository = activityRepository;
        this.mapper = mapper;
    }

    @Override
    public ActivityResult execute(
            ContactId contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt) {
        if (contactRepository.findById(contactId).isEmpty()) {
            throw new ContactNotFoundException(contactId);
        }
        Activity activity = Activity.create(
                ActivityId.generate(), contactId, authorUserId, activityType, description, occurredAt);
        return mapper.toActivityResult(activityRepository.save(activity));
    }
}
