package com.example.contact.infrastructure.storage;

import com.example.contact.domain.port.AvatarStoragePort;
import com.example.contact.domain.valueobject.ContactId;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Stores avatars on local filesystem and returns public URL path.
 */
@Component
@ConditionalOnProperty(name = "app.avatar.provider", havingValue = "local")
public class LocalAvatarStorageAdapter implements AvatarStoragePort {

    private final Path storageDir;

    public LocalAvatarStorageAdapter(@Value("${app.avatar.storage-path:./data/avatars}") String storagePath) {
        this.storageDir = Path.of(storagePath);
    }

    @Override
    public String store(ContactId contactId, String contentType, byte[] data) {
        try {
            Files.createDirectories(storageDir);
            String extension = extensionFromContentType(contentType);
            String fileName = contactId.value() + extension;
            Path target = storageDir.resolve(fileName);
            Files.write(target, data);
            return "/avatars/" + fileName;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store avatar", e);
        }
    }

    private String extensionFromContentType(String contentType) {
        if (contentType == null) {
            return ".bin";
        }
        return switch (contentType.toLowerCase(Locale.ROOT)) {
            case "image/png" -> ".png";
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".bin";
        };
    }
}
