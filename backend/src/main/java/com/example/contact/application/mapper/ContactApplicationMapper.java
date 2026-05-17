package com.example.contact.application.mapper;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.command.ContactResult;
import com.example.contact.application.command.NoteResult;
import com.example.contact.application.command.TagResult;
import com.example.contact.domain.model.Activity;
import com.example.contact.domain.model.Contact;
import com.example.contact.domain.model.Note;
import com.example.contact.domain.model.Tag;
import com.example.contact.domain.valueobject.Address;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps domain aggregates to application-layer results.
 */
@Mapper(componentModel = "spring")
public interface ContactApplicationMapper {

    @Mapping(target = "id", expression = "java(contact.id())")
    @Mapping(target = "firstName", expression = "java(contact.firstName())")
    @Mapping(target = "lastName", expression = "java(contact.lastName())")
    @Mapping(target = "email", expression = "java(contact.email().value())")
    @Mapping(
            target = "phone",
            expression = "java(contact.phone() == null ? null : contact.phone().value())")
    @Mapping(target = "company", expression = "java(contact.company())")
    @Mapping(target = "jobTitle", expression = "java(contact.jobTitle())")
    @Mapping(target = "street", expression = "java(street(contact))")
    @Mapping(target = "city", expression = "java(city(contact))")
    @Mapping(target = "postalCode", expression = "java(postalCode(contact))")
    @Mapping(target = "country", expression = "java(country(contact))")
    @Mapping(target = "avatarUrl", expression = "java(contact.avatarUrl())")
    @Mapping(target = "status", expression = "java(contact.status())")
    @Mapping(target = "assignedToUserId", expression = "java(contact.assignedToUserId())")
    @Mapping(target = "createdAt", expression = "java(contact.createdAt())")
    @Mapping(target = "updatedAt", expression = "java(contact.updatedAt())")
    @Mapping(target = "notes", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "activities", expression = "java(java.util.Collections.emptyList())")
    @Mapping(target = "tags", expression = "java(java.util.Collections.emptyList())")
    ContactResult toResult(Contact contact);

  default ContactResult toDetailResult(
      Contact contact, List<Note> notes, List<Activity> activities, List<Tag> tags) {
    return ContactResult.builder()
        .id(contact.id())
        .firstName(contact.firstName())
        .lastName(contact.lastName())
        .email(contact.email().value())
        .phone(contact.phone() == null ? null : contact.phone().value())
        .company(contact.company())
        .jobTitle(contact.jobTitle())
        .street(street(contact))
        .city(city(contact))
        .postalCode(postalCode(contact))
        .country(country(contact))
        .avatarUrl(contact.avatarUrl())
        .status(contact.status())
        .assignedToUserId(contact.assignedToUserId())
        .createdAt(contact.createdAt())
        .updatedAt(contact.updatedAt())
        .notes(notes == null ? Collections.emptyList() : notes.stream().map(this::toNoteResult).toList())
        .activities(
            activities == null
                ? Collections.emptyList()
                : activities.stream().map(this::toActivityResult).toList())
        .tags(tags == null ? Collections.emptyList() : tags.stream().map(this::toTagResult).toList())
        .build();
  }

  default String street(Contact contact) {
    Address address = contact.address();
    return address == null ? null : address.street();
  }

  default String city(Contact contact) {
    Address address = contact.address();
    return address == null ? null : address.city();
  }

  default String postalCode(Contact contact) {
    Address address = contact.address();
    return address == null ? null : address.postalCode();
  }

  default String country(Contact contact) {
    Address address = contact.address();
    return address == null ? null : address.country();
  }

  default NoteResult toNoteResult(Note note) {
    return NoteResult.builder()
        .id(note.id().value().toString())
        .content(note.content())
        .authorUserId(note.authorUserId())
        .createdAt(note.createdAt())
        .updatedAt(note.updatedAt())
        .build();
  }

  default ActivityResult toActivityResult(Activity activity) {
    return ActivityResult.builder()
        .id(activity.id().value().toString())
        .activityType(activity.activityType())
        .description(activity.description())
        .authorUserId(activity.authorUserId())
        .occurredAt(activity.occurredAt())
        .createdAt(activity.createdAt())
        .build();
  }

  default TagResult toTagResult(Tag tag) {
    return TagResult.builder().id(tag.id().value().toString()).name(tag.name()).build();
  }
}
