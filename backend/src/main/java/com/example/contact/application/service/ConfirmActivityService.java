package com.example.contact.application.service;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.ConfirmActivityUseCase;
import com.example.contact.domain.exception.ActivityAlreadyConfirmedException;
import com.example.contact.domain.exception.ActivityNotFoundException;
import com.example.contact.domain.model.Activity;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;

public final class ConfirmActivityService implements ConfirmActivityUseCase {

    private final ActivityRepository activityRepository;
    private final ContactApplicationMapper mapper;

    public ConfirmActivityService(ActivityRepository activityRepository, ContactApplicationMapper mapper) {
        this.activityRepository = activityRepository;
        this.mapper = mapper;
    }

    @Override
    public ActivityResult execute(ContactId contactId, ActivityId activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));
        if (!activity.contactId().equals(contactId)) {
            throw new ActivityNotFoundException(activityId);
        }
        if (activity.confirmed()) {
            throw new ActivityAlreadyConfirmedException(activityId);
        }
        return mapper.toActivityResult(activityRepository.save(activity.confirm()));
    }
}
