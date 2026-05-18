package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Activity;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.query.ActivityListCriteria;
import com.example.contact.domain.query.PagedActivities;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class JpaActivityRepositoryAdapter implements ActivityRepository {

    private final SpringDataActivityRepository repository;

    public JpaActivityRepositoryAdapter(SpringDataActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Activity> findByContactId(ContactId contactId) {
        return repository.findByContactIdOrderByOccurredAtDesc(contactId.value()).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public PagedActivities search(ContactId contactId, ActivityListCriteria criteria) {
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize());
        String activityType = criteria.getActivityType();
        Page<ActivityJpaEntity> page;
        if (activityType == null || activityType.isBlank()) {
            page = repository.findByContactIdOrderByOccurredAtDesc(contactId.value(), pageRequest);
        } else {
            page = repository.findByContactIdAndActivityTypeIgnoreCaseOrderByOccurredAtDesc(
                    contactId.value(), activityType.trim(), pageRequest);
        }
        return PagedActivities.builder()
                .content(page.getContent().stream().map(this::toDomain).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .build();
    }

    @Override
    public Optional<Activity> findById(ActivityId activityId) {
        return repository.findById(activityId.value()).map(this::toDomain);
    }

    @Override
    public Activity save(Activity activity) {
        ActivityJpaEntity saved = repository.save(toEntity(activity));
        return toDomain(saved);
    }

    @Override
    public void delete(ActivityId activityId) {
        repository.deleteById(activityId.value());
    }

    private Activity toDomain(ActivityJpaEntity entity) {
        return Activity.restore(
                ActivityId.of(entity.getId().toString()),
                ContactId.of(entity.getContactId().toString()),
                entity.getAuthorUserId(),
                entity.getActivityType(),
                entity.getDescription(),
                entity.getOccurredAt(),
                entity.getCreatedAt(),
                entity.isConfirmed());
    }

    private ActivityJpaEntity toEntity(Activity activity) {
        return ActivityJpaEntity.builder()
                .id(activity.id().value())
                .contactId(activity.contactId().value())
                .authorUserId(activity.authorUserId())
                .activityType(activity.activityType())
                .description(activity.description())
                .occurredAt(activity.occurredAt())
                .createdAt(activity.createdAt())
                .confirmed(activity.confirmed())
                .build();
    }
}
