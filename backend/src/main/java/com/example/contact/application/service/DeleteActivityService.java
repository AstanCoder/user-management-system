package com.example.contact.application.service;

import com.example.contact.application.port.in.DeleteActivityUseCase;
import com.example.contact.domain.exception.ActivityDeletionNotAllowedException;
import com.example.contact.domain.exception.ActivityNotFoundException;
import com.example.contact.domain.model.Activity;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;

public final class DeleteActivityService implements DeleteActivityUseCase {

    private final ActivityRepository activityRepository;

    public DeleteActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void execute(ContactId contactId, ActivityId activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(activityId));
        if (!activity.contactId().equals(contactId)) {
            throw new ActivityNotFoundException(activityId);
        }
        if (activity.confirmed()) {
            throw new ActivityDeletionNotAllowedException(activityId);
        }
        activityRepository.delete(activityId);
    }
}
