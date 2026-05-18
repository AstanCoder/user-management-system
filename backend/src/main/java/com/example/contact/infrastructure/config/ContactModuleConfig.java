package com.example.contact.infrastructure.config;

import com.example.contact.application.mapper.ContactApplicationMapper;
import com.example.contact.application.port.in.AddNoteUseCase;
import com.example.contact.application.port.in.AssignTagsUseCase;
import com.example.contact.application.port.in.ConfirmActivityUseCase;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteActivityUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.DeleteNoteUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.ListNotesUseCase;
import com.example.contact.application.port.in.LogActivityUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.application.port.in.UploadAvatarUseCase;
import com.example.contact.application.service.AddNoteService;
import com.example.contact.application.service.AssignTagsService;
import com.example.contact.application.service.ConfirmActivityService;
import com.example.contact.application.service.CreateContactService;
import com.example.contact.application.service.DeleteActivityService;
import com.example.contact.application.service.DeleteContactService;
import com.example.contact.application.service.DeleteNoteService;
import com.example.contact.application.service.GetContactService;
import com.example.contact.application.service.ListActivitiesService;
import com.example.contact.application.service.ListContactsService;
import com.example.contact.application.service.ListNotesService;
import com.example.contact.application.service.LogActivityService;
import com.example.contact.application.service.UpdateContactService;
import com.example.contact.application.service.UploadAvatarService;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.port.AvatarStoragePort;
import com.example.contact.domain.port.ContactRepository;
import com.example.contact.domain.port.DomainEventPublisher;
import com.example.contact.domain.port.NoteRepository;
import com.example.contact.domain.port.TagRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Composition root wiring use cases to outbound port implementations.
 */
@Configuration
public class ContactModuleConfig {

    @Bean
    public CreateContactUseCase createContactUseCase(
            ContactRepository contactRepository,
            DomainEventPublisher eventPublisher,
            ContactApplicationMapper contactApplicationMapper) {
        return new CreateContactService(contactRepository, eventPublisher, contactApplicationMapper);
    }

    @Bean
    public ListContactsUseCase listContactsUseCase(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        return new ListContactsService(contactRepository, contactApplicationMapper);
    }

    @Bean
    public GetContactUseCase getContactUseCase(
            ContactRepository contactRepository,
            NoteRepository noteRepository,
            ActivityRepository activityRepository,
            TagRepository tagRepository,
            ContactApplicationMapper contactApplicationMapper) {
        return new GetContactService(
                contactRepository, noteRepository, activityRepository, tagRepository, contactApplicationMapper);
    }

    @Bean
    public UpdateContactUseCase updateContactUseCase(
            ContactRepository contactRepository, ContactApplicationMapper contactApplicationMapper) {
        return new UpdateContactService(contactRepository, contactApplicationMapper);
    }

    @Bean
    public DeleteContactUseCase deleteContactUseCase(ContactRepository contactRepository) {
        return new DeleteContactService(contactRepository);
    }

    @Bean
    public AddNoteUseCase addNoteUseCase(
            ContactRepository contactRepository,
            NoteRepository noteRepository,
            ContactApplicationMapper contactApplicationMapper) {
        return new AddNoteService(contactRepository, noteRepository, contactApplicationMapper);
    }

    @Bean
    public ListNotesUseCase listNotesUseCase(NoteRepository noteRepository, ContactApplicationMapper mapper) {
        return new ListNotesService(noteRepository, mapper);
    }

    @Bean
    public LogActivityUseCase logActivityUseCase(
            ContactRepository contactRepository,
            ActivityRepository activityRepository,
            ContactApplicationMapper mapper) {
        return new LogActivityService(contactRepository, activityRepository, mapper);
    }

    @Bean
    public ListActivitiesUseCase listActivitiesUseCase(
            ActivityRepository activityRepository, ContactApplicationMapper mapper) {
        return new ListActivitiesService(activityRepository, mapper);
    }

    @Bean
    public ConfirmActivityUseCase confirmActivityUseCase(
            ActivityRepository activityRepository, ContactApplicationMapper mapper) {
        return new ConfirmActivityService(activityRepository, mapper);
    }

    @Bean
    public DeleteActivityUseCase deleteActivityUseCase(ActivityRepository activityRepository) {
        return new DeleteActivityService(activityRepository);
    }

    @Bean
    public DeleteNoteUseCase deleteNoteUseCase(NoteRepository noteRepository) {
        return new DeleteNoteService(noteRepository);
    }

    @Bean
    public AssignTagsUseCase assignTagsUseCase(
            ContactRepository contactRepository, TagRepository tagRepository, ContactApplicationMapper mapper) {
        return new AssignTagsService(contactRepository, tagRepository, mapper);
    }

    @Bean
    public UploadAvatarUseCase uploadAvatarUseCase(
            ContactRepository contactRepository,
            AvatarStoragePort avatarStoragePort,
            ContactApplicationMapper mapper) {
        return new UploadAvatarService(contactRepository, avatarStoragePort, mapper);
    }
}
