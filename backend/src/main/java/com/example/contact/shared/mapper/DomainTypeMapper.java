package com.example.contact.shared.mapper;

import com.example.contact.domain.model.Contact;
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

    /**
     * Converts a domain contact id to UUID for persistence.
     *
     * @param contactId domain id
     * @return uuid
     */
    @Named("contactIdToUuid")
    public UUID contactIdToUuid(ContactId contactId) {
        return contactId.value();
    }

    /**
     * Converts UUID to a domain contact id.
     *
     * @param id uuid
     * @return domain id
     */
    @Named("uuidToContactId")
    public ContactId uuidToContactId(UUID id) {
        return ContactId.of(id.toString());
    }

    /**
     * Converts path string to domain contact id.
     *
     * @param id uuid string
     * @return domain id
     */
    @Named("stringToContactId")
    public ContactId stringToContactId(String id) {
        return ContactId.of(id);
    }

    /**
     * Converts domain contact id to string for REST responses.
     *
     * @param contactId domain id
     * @return uuid string
     */
    @Named("contactIdToString")
    public String contactIdToString(ContactId contactId) {
        return contactId.value().toString();
    }

    /**
     * Converts email value object to string.
     *
     * @param email domain email
     * @return email string
     */
    @Named("emailToString")
    public String emailToString(Email email) {
        return email.value();
    }

    /**
     * Rehydrates email from stored string.
     *
     * @param value stored email
     * @return email value object
     */
    @Named("stringToEmail")
    public Email stringToEmail(String value) {
        return Email.fromStored(value);
    }

    /**
     * Converts optional phone value object to string.
     *
     * @param phone domain phone or null
     * @return phone string or null
     */
    @Named("phoneToString")
    public String phoneToString(PhoneNumber phone) {
        return phone == null ? null : phone.value();
    }

    /**
     * Rehydrates phone from stored string.
     *
     * @param value stored phone or null
     * @return phone value object or null
     */
    @Named("stringToPhone")
    public PhoneNumber stringToPhone(String value) {
        return value == null ? null : PhoneNumber.fromStored(value);
    }

    /**
     * Rebuilds a domain aggregate from a JPA entity.
     *
     * @param entity persistence entity
     * @return domain contact
     */
    public Contact toContact(ContactJpaEntity entity) {
        return Contact.restore(
                uuidToContactId(entity.getId()),
                entity.getFirstName(),
                entity.getLastName(),
                stringToEmail(entity.getEmail()),
                stringToPhone(entity.getPhone()),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
