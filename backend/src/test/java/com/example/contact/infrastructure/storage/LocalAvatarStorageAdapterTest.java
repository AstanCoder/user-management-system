package com.example.contact.infrastructure.storage;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.contact.domain.valueobject.ContactId;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LocalAvatarStorageAdapterTest {

    @TempDir
    java.nio.file.Path tempDir;

    @Test
    void store_sameContactTwice_returnsDifferentAvatarUrls() throws Exception {
        var adapter = new LocalAvatarStorageAdapter(tempDir.toString());
        var contactId = ContactId.of("8a0f8cb5-8d70-4a69-a3ca-140f64ee817d");
        byte[] firstAvatar = "first".getBytes();
        byte[] secondAvatar = "second".getBytes();

        String firstUrl = adapter.store(contactId, "image/png", firstAvatar);
        String secondUrl = adapter.store(contactId, "image/png", secondAvatar);

        assertNotEquals(firstUrl, secondUrl);
        assertTrue(Files.exists(tempDir.resolve(firstUrl.replace("/avatars/", ""))));
        assertTrue(Files.exists(tempDir.resolve(secondUrl.replace("/avatars/", ""))));
    }
}
