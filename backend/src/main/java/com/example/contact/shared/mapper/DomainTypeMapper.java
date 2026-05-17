package com.example.contact.shared.mapper;

import com.example.contact.domain.model.Contact;
import com.example.contact.domain.model.ContactStatus;
import com.example.contact.domain.valueobject.Address;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.domain.valueobject.PhoneNumber;
import com.example.contact.infrastructure.persistence.ContactJpaEntity;
import java.util.UUID;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

/**
 * MapStruct helper for converting between domain value objects and primitive or persistence types.
 */
@Component
public class DomainTypeMapper {

  @Named("contactIdToUuid")
  public UUID contactIdToUuid(ContactId contactId) {
    return contactId.value();
  }

  @Named("uuidToContactId")
  public ContactId uuidToContactId(UUID id) {
    return ContactId.of(id.toString());
  }

  @Named("stringToContactId")
  public ContactId stringToContactId(String id) {
    return ContactId.of(id);
  }

  @Named("contactIdToString")
  public String contactIdToString(ContactId contactId) {
    return contactId.value().toString();
  }

  @Named("emailToString")
  public String emailToString(Email email) {
    return email.value();
  }

  @Named("stringToEmail")
  public Email stringToEmail(String value) {
    return Email.fromStored(value);
  }

  @Named("phoneToString")
  public String phoneToString(PhoneNumber phone) {
    return phone == null ? null : phone.value();
  }

  @Named("stringToPhone")
  public PhoneNumber stringToPhone(String value) {
    return value == null ? null : PhoneNumber.fromStored(value);
  }

  @Named("stringToContactStatus")
  public ContactStatus stringToContactStatus(String value) {
    return value == null ? null : ContactStatus.valueOf(value);
  }

  @Named("stringToUuid")
  public UUID stringToUuid(String value) {
    return value == null || value.isBlank() ? null : UUID.fromString(value);
  }

  public Contact toContact(ContactJpaEntity entity) {
    Address address =
        Address.fromStored(entity.getStreet(), entity.getCity(), entity.getPostalCode(), entity.getCountry());
    return Contact.restore(
        uuidToContactId(entity.getId()),
        entity.getFirstName(),
        entity.getLastName(),
        stringToEmail(entity.getEmail()),
        stringToPhone(entity.getPhone()),
        entity.getCompany(),
        entity.getJobTitle(),
        address,
        entity.getAvatarUrl(),
        ContactStatus.valueOf(entity.getStatus()),
        entity.getAssignedToUserId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt());
  }
}
