package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.query.ContactSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

/**
 * JPA specification for contact search.
 */
public final class ContactSearchSpecification {

    private ContactSearchSpecification() {}

    /**
     * Builds specification from search criteria.
     *
     * @param criteria search criteria
     * @return specification
     */
    public static Specification<ContactJpaEntity> from(ContactSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getSearch() != null && !criteria.getSearch().isBlank()) {
                String pattern = "%" + criteria.getSearch().trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern),
                        cb.like(cb.lower(root.get("email")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("phone"), "")), pattern),
                        cb.like(cb.lower(cb.coalesce(root.get("company"), "")), pattern)));
            }
            if (criteria.getEmail() != null && !criteria.getEmail().isBlank()) {
                String emailPattern = "%" + criteria.getEmail().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("email")), emailPattern));
            }
            if (criteria.getPhone() != null && !criteria.getPhone().isBlank()) {
                String phonePattern = "%" + criteria.getPhone().trim().toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(cb.coalesce(root.get("phone"), "")), phonePattern));
            }
            if (criteria.getTagNames() != null && query != null) {
                List<String> normalizedTags = criteria.getTagNames().stream()
                        .filter(tag -> tag != null && !tag.isBlank())
                        .map(tag -> tag.trim().toLowerCase())
                        .toList();
                if (!normalizedTags.isEmpty()) {
                    var tagExists = query.subquery(UUID.class);
                    var contactTagRoot = tagExists.from(ContactTagJpaEntity.class);
                    var tagRoot = tagExists.from(TagJpaEntity.class);
                    tagExists.select(contactTagRoot.get("contactId"))
                            .where(
                                    cb.equal(contactTagRoot.get("contactId"), root.get("id")),
                                    cb.equal(contactTagRoot.get("tagId"), tagRoot.get("id")),
                                    cb.lower(tagRoot.get("name")).in(normalizedTags));
                    predicates.add(cb.exists(tagExists));
                }
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
