package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.query.ContactSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
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
                        cb.like(cb.lower(cb.coalesce(root.get("company"), "")), pattern)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
