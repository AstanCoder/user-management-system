package com.example.contact.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contact_tags")
@IdClass(ContactTagJpaEntity.ContactTagId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactTagJpaEntity {

    @Id
    @Column(name = "contact_id", nullable = false)
    private UUID contactId;

    @Id
    @Column(name = "tag_id", nullable = false)
    private UUID tagId;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactTagId implements Serializable {
        private UUID contactId;
        private UUID tagId;
    }
}
