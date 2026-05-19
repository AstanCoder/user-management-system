package com.example.infrastructure.seed;

import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DemoDataSeeder {

    private static final String DEFAULT_PASSWORD_HASH =
            "$2y$10$RZqepUu2svbE5RvppWSxherkbnWASxWfuQPlLTCq.ANVNYui1H18q";
    private static final int INSERT_BATCH_SIZE = 250;
    private static final List<String> TAG_NAMES = List.of(
            "VIP",
            "Prospect",
            "Customer",
            "Churn-risk",
            "SMB",
            "Enterprise",
            "Renewal-2026",
            "Partner",
            "Cold-lead",
            "Warm-lead",
            "Newsletter",
            "Webinar",
            "Upsell-candidate",
            "Cross-sell",
            "LATAM",
            "North-America",
            "Europe",
            "Ecommerce",
            "SaaS",
            "Healthcare");
    private static final List<String> FIRST_NAMES = List.of(
            "Liam",
            "Olivia",
            "Noah",
            "Emma",
            "Mateo",
            "Sofia",
            "Lucas",
            "Isabella",
            "Ethan",
            "Mia",
            "Ava",
            "Logan",
            "Amelia",
            "Elijah",
            "Harper",
            "James",
            "Benjamin",
            "Charlotte",
            "Daniel",
            "Victoria");
    private static final List<String> LAST_NAMES = List.of(
            "Smith",
            "Johnson",
            "Brown",
            "Taylor",
            "Anderson",
            "Martinez",
            "Garcia",
            "Clark",
            "Lewis",
            "Walker",
            "Hall",
            "Allen",
            "Young",
            "King",
            "Wright",
            "Scott",
            "Green",
            "Baker",
            "Adams",
            "Nelson");
    private static final List<String> COMPANIES = List.of(
            "Helios Analytics",
            "Northwind Foods",
            "Vertex Dynamics",
            "BlueRock Logistics",
            "Nimbus Health",
            "Copperline Retail",
            "Aster Financial",
            "Delta Mobility",
            "Atlas Security",
            "Orbit Learning");
    private static final List<String> JOB_TITLES = List.of(
            "Operations Manager",
            "Head of Sales",
            "Business Analyst",
            "Procurement Lead",
            "Marketing Director",
            "Customer Success Manager",
            "HR Business Partner",
            "Finance Manager",
            "Regional Manager",
            "Product Owner");
    private static final List<String> STREETS = List.of(
            "Main St",
            "Broadway",
            "Oak Ave",
            "Maple Dr",
            "Cedar Ln",
            "Park Blvd",
            "Pine St",
            "Lakeview Rd",
            "Sunset Ave",
            "River Rd");
    private static final List<String> CITIES = List.of(
            "New York",
            "Austin",
            "Miami",
            "Chicago",
            "Seattle",
            "Denver",
            "Toronto",
            "Bogota",
            "Madrid",
            "Santiago");
    private static final List<String> COUNTRIES = List.of(
            "United States",
            "Canada",
            "Colombia",
            "Chile",
            "Spain",
            "Mexico");
    private static final List<String> ACTIVITY_TYPES = List.of(
            "CALL",
            "EMAIL",
            "MEETING",
            "FOLLOW_UP");
    private static final List<String> ACTIVITY_DESCRIPTIONS = List.of(
            "Discovery conversation with stakeholder",
            "Shared proposal and pricing details",
            "Quarterly review completed",
            "Requested implementation timeline",
            "Confirmed budget approval path",
            "Follow-up after product demo");
    private static final List<String> NOTE_TEMPLATES = List.of(
            "Customer asked for references from a similar vertical.",
            "Prefers communication via email and monthly checkpoints.",
            "Interested in onboarding support for distributed teams.",
            "Decision timeline moved to next quarter.",
            "Wants a bundled offer with additional seats.");

    private final JdbcTemplate jdbcTemplate;
    private final DemoSeedProperties properties;

    public DemoDataSeeder(JdbcTemplate jdbcTemplate, DemoSeedProperties properties) {
        this.jdbcTemplate = jdbcTemplate;
        this.properties = properties;
    }

    public void seed() {
        int existingContacts = getCount("contacts");
        int contactsToCreate = Math.max(0, properties.getTargetContacts() - existingContacts);
        if (contactsToCreate == 0) {
            return;
        }

        Random random = new Random(20260518L);
        List<UUID> userIds = ensureUsers(random);
        Map<String, UUID> tagIds = ensureTags(random);
        Instant now = Instant.now();

        List<ContactRow> contacts = buildContacts(contactsToCreate, userIds, now, random);
        batchInsertContacts(contacts);

        List<ContactTagRow> contactTags = buildContactTags(contacts, tagIds, random);
        batchInsertContactTags(contactTags);

        List<NoteRow> notes = buildNotes(contacts, userIds, now, random);
        batchInsertNotes(notes);

        List<ActivityRow> activities = buildActivities(contacts, userIds, now, random);
        batchInsertActivities(activities);
    }

    private int getCount(String tableName) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from " + tableName, Integer.class);
        return count == null ? 0 : count;
    }

    private List<UUID> ensureUsers(Random random) {
        Integer existingUsers = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        int currentUsers = existingUsers == null ? 0 : existingUsers;
        int usersToCreate = Math.max(0, properties.getTargetUsers() - currentUsers);
        if (usersToCreate > 0) {
            Instant now = Instant.now();
            List<UserRow> rows = new ArrayList<>(usersToCreate);
            for (int i = 0; i < usersToCreate; i++) {
                int index = currentUsers + i + 1;
                UUID id = stableUuid("seed-user-" + index);
                String firstName = pick(FIRST_NAMES, random);
                String lastName = pick(LAST_NAMES, random);
                String role = index % 8 == 0 ? "VIEWER" : "EDITOR";
                String email = "seed.user." + index + "@nexuscrm.com";
                Instant createdAt = now.minus(index, ChronoUnit.DAYS);
                rows.add(new UserRow(
                        id,
                        email,
                        firstName,
                        lastName,
                        role,
                        "ACTIVE",
                        createdAt,
                        createdAt.plus(3, ChronoUnit.HOURS),
                        now.minus(random.nextInt(21), ChronoUnit.DAYS)));
            }
            batchInsertUsers(rows);
        }
        return jdbcTemplate.query(
                "select id from users where status = 'ACTIVE'",
                (rs, rowNum) -> rs.getObject("id", UUID.class));
    }

    private Map<String, UUID> ensureTags(Random random) {
        Instant createdAt = Instant.now().minus(90, ChronoUnit.DAYS);
        List<TagRow> rows = new ArrayList<>(TAG_NAMES.size());
        for (String tagName : TAG_NAMES) {
            rows.add(new TagRow(stableUuid("seed-tag-" + tagName), tagName, createdAt.plus(random.nextInt(10), ChronoUnit.DAYS)));
        }
        batchInsertTags(rows);

        Map<String, UUID> tags = new LinkedHashMap<>();
        jdbcTemplate.query("select id, name from tags", rs -> {
            String name = rs.getString("name");
            if (TAG_NAMES.contains(name)) {
                tags.put(name, rs.getObject("id", UUID.class));
            }
        });
        return tags;
    }

    private List<ContactRow> buildContacts(int size, List<UUID> userIds, Instant now, Random random) {
        List<ContactRow> rows = new ArrayList<>(size);
        int existing = getCount("contacts");
        for (int i = 0; i < size; i++) {
            int index = existing + i + 1;
            String firstName = pick(FIRST_NAMES, random);
            String lastName = pick(LAST_NAMES, random);
            String company = pick(COMPANIES, random);
            Instant createdAt = now.minus(random.nextInt(730), ChronoUnit.DAYS);
            rows.add(new ContactRow(
                    stableUuid("seed-contact-" + index),
                    firstName,
                    lastName,
                    "contact." + index + "@example-crm.com",
                    buildPhone(index),
                    createdAt,
                    createdAt.plus(random.nextInt(45), ChronoUnit.DAYS),
                    company,
                    pick(JOB_TITLES, random),
                    (10 + random.nextInt(999)) + " " + pick(STREETS, random),
                    pick(CITIES, random),
                    String.valueOf(10000 + random.nextInt(89999)),
                    pick(COUNTRIES, random),
                    null,
                    random.nextInt(10) == 0 ? "LEAD" : (random.nextInt(12) == 0 ? "INACTIVE" : "ACTIVE"),
                    userIds.get(random.nextInt(userIds.size()))));
        }
        return rows;
    }

    private List<ContactTagRow> buildContactTags(List<ContactRow> contacts, Map<String, UUID> tagIds, Random random) {
        List<UUID> values = new ArrayList<>(tagIds.values());
        List<ContactTagRow> rows = new ArrayList<>();
        for (ContactRow contact : contacts) {
            int tagCount = 1 + random.nextInt(3);
            List<UUID> assigned = new ArrayList<>(tagCount);
            while (assigned.size() < tagCount) {
                UUID tagId = values.get(random.nextInt(values.size()));
                if (!assigned.contains(tagId)) {
                    assigned.add(tagId);
                    rows.add(new ContactTagRow(contact.id(), tagId));
                }
            }
        }
        return rows;
    }

    private List<NoteRow> buildNotes(List<ContactRow> contacts, List<UUID> userIds, Instant now, Random random) {
        List<NoteRow> rows = new ArrayList<>();
        for (ContactRow contact : contacts) {
            int noteCount = random.nextInt(3);
            for (int i = 0; i < noteCount; i++) {
                Instant createdAt = contact.createdAt().plus(random.nextInt(180), ChronoUnit.DAYS);
                Instant boundedCreatedAt = createdAt.isAfter(now) ? now.minus(random.nextInt(48), ChronoUnit.HOURS) : createdAt;
                rows.add(new NoteRow(
                        stableUuid("seed-note-" + contact.id() + "-" + i),
                        contact.id(),
                        userIds.get(random.nextInt(userIds.size())),
                        pick(NOTE_TEMPLATES, random),
                        boundedCreatedAt,
                        boundedCreatedAt.plus(random.nextInt(12), ChronoUnit.HOURS)));
            }
        }
        return rows;
    }

    private List<ActivityRow> buildActivities(List<ContactRow> contacts, List<UUID> userIds, Instant now, Random random) {
        List<ActivityRow> rows = new ArrayList<>();
        for (ContactRow contact : contacts) {
            int activityCount = 1 + random.nextInt(4);
            for (int i = 0; i < activityCount; i++) {
                Instant occurredAt = contact.createdAt().plus(random.nextInt(240), ChronoUnit.DAYS);
                Instant boundedOccurredAt = occurredAt.isAfter(now) ? now.minus(random.nextInt(72), ChronoUnit.HOURS) : occurredAt;
                rows.add(new ActivityRow(
                        stableUuid("seed-activity-" + contact.id() + "-" + i),
                        contact.id(),
                        userIds.get(random.nextInt(userIds.size())),
                        pick(ACTIVITY_TYPES, random),
                        pick(ACTIVITY_DESCRIPTIONS, random),
                        boundedOccurredAt,
                        boundedOccurredAt.plus(random.nextInt(8), ChronoUnit.HOURS),
                        random.nextInt(10) != 0));
            }
        }
        return rows;
    }

    private void batchInsertUsers(List<UserRow> rows) {
        String sql = """
                insert into users (id, email, password_hash, first_name, last_name, role, status, created_at, updated_at, last_active_at)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.id());
            statement.setString(2, row.email());
            statement.setString(3, DEFAULT_PASSWORD_HASH);
            statement.setString(4, row.firstName());
            statement.setString(5, row.lastName());
            statement.setString(6, row.role());
            statement.setString(7, row.status());
            statement.setTimestamp(8, Timestamp.from(row.createdAt()));
            statement.setTimestamp(9, Timestamp.from(row.updatedAt()));
            statement.setTimestamp(10, Timestamp.from(row.lastActiveAt()));
        }, sql);
    }

    private void batchInsertTags(List<TagRow> rows) {
        String sql = """
                insert into tags (id, name, created_at)
                values (?, ?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.id());
            statement.setString(2, row.name());
            statement.setTimestamp(3, Timestamp.from(row.createdAt()));
        }, sql);
    }

    private void batchInsertContacts(List<ContactRow> rows) {
        String sql = """
                insert into contacts (id, first_name, last_name, email, phone, created_at, updated_at, company, job_title, street, city, postal_code, country, avatar_url, status, assigned_to_user_id)
                values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.id());
            statement.setString(2, row.firstName());
            statement.setString(3, row.lastName());
            statement.setString(4, row.email());
            statement.setString(5, row.phone());
            statement.setTimestamp(6, Timestamp.from(row.createdAt()));
            statement.setTimestamp(7, Timestamp.from(row.updatedAt()));
            statement.setString(8, row.company());
            statement.setString(9, row.jobTitle());
            statement.setString(10, row.street());
            statement.setString(11, row.city());
            statement.setString(12, row.postalCode());
            statement.setString(13, row.country());
            statement.setString(14, row.avatarUrl());
            statement.setString(15, row.status());
            statement.setObject(16, row.assignedToUserId());
        }, sql);
    }

    private void batchInsertContactTags(List<ContactTagRow> rows) {
        String sql = """
                insert into contact_tags (contact_id, tag_id)
                values (?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.contactId());
            statement.setObject(2, row.tagId());
        }, sql);
    }

    private void batchInsertNotes(List<NoteRow> rows) {
        String sql = """
                insert into notes (id, contact_id, author_user_id, content, created_at, updated_at)
                values (?, ?, ?, ?, ?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.id());
            statement.setObject(2, row.contactId());
            statement.setObject(3, row.authorUserId());
            statement.setString(4, row.content());
            statement.setTimestamp(5, Timestamp.from(row.createdAt()));
            statement.setTimestamp(6, Timestamp.from(row.updatedAt()));
        }, sql);
    }

    private void batchInsertActivities(List<ActivityRow> rows) {
        String sql = """
                insert into activities (id, contact_id, author_user_id, activity_type, description, occurred_at, created_at, confirmed)
                values (?, ?, ?, ?, ?, ?, ?, ?)
                on conflict do nothing
                """;
        batchInsert(rows, (statement, row) -> {
            statement.setObject(1, row.id());
            statement.setObject(2, row.contactId());
            statement.setObject(3, row.authorUserId());
            statement.setString(4, row.activityType());
            statement.setString(5, row.description());
            statement.setTimestamp(6, Timestamp.from(row.occurredAt()));
            statement.setTimestamp(7, Timestamp.from(row.createdAt()));
            statement.setBoolean(8, row.confirmed());
        }, sql);
    }

    private <T> void batchInsert(List<T> rows, PreparedStatementBinder<T> binder, String sql) {
        int fromIndex = 0;
        while (fromIndex < rows.size()) {
            int toIndex = Math.min(fromIndex + INSERT_BATCH_SIZE, rows.size());
            List<T> chunk = rows.subList(fromIndex, toIndex);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    binder.bind(ps, chunk.get(i));
                }

                @Override
                public int getBatchSize() {
                    return chunk.size();
                }
            });
            fromIndex = toIndex;
        }
    }

    private String buildPhone(int index) {
        return "+1-555-" + String.format("%04d", index % 10000);
    }

    private UUID stableUuid(String value) {
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    private String pick(List<String> values, Random random) {
        return values.get(random.nextInt(values.size()));
    }

    private record UserRow(
            UUID id,
            String email,
            String firstName,
            String lastName,
            String role,
            String status,
            Instant createdAt,
            Instant updatedAt,
            Instant lastActiveAt) {
    }

    private record TagRow(UUID id, String name, Instant createdAt) {
    }

    private record ContactRow(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String phone,
            Instant createdAt,
            Instant updatedAt,
            String company,
            String jobTitle,
            String street,
            String city,
            String postalCode,
            String country,
            String avatarUrl,
            String status,
            UUID assignedToUserId) {
    }

    private record ContactTagRow(UUID contactId, UUID tagId) {
    }

    private record NoteRow(
            UUID id,
            UUID contactId,
            UUID authorUserId,
            String content,
            Instant createdAt,
            Instant updatedAt) {
    }

    private record ActivityRow(
            UUID id,
            UUID contactId,
            UUID authorUserId,
            String activityType,
            String description,
            Instant occurredAt,
            Instant createdAt,
            boolean confirmed) {
    }

    @FunctionalInterface
    private interface PreparedStatementBinder<T> {
        void bind(PreparedStatement statement, T row) throws SQLException;
    }
}
