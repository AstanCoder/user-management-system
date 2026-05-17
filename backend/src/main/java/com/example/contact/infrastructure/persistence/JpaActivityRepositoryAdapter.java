package com.example.contact.infrastructure.persistence;

import com.example.contact.domain.model.Activity;
import com.example.contact.domain.port.ActivityRepository;
import com.example.contact.domain.valueobject.ActivityId;
import com.example.contact.domain.valueobject.ContactId;
import java.util.List;
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
    public Activity save(Activity activity) {
        ActivityJpaEntity saved = repository.save(toEntity(activity));
        return toDomain(saved);
    }

    private Activity toDomain(ActivityJpaEntity entity) {
        return Activity.restore(
                ActivityId.of(entity.getId().toString()),
                ContactId.of(entity.getContactId().toString()),
                entity.getAuthorUserId(),
                entity.getActivityType(),
                entity.getDescription(),
                entity.getOccurredAt(),
                entity.getCreatedAt());
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
                .build();
    }
}
