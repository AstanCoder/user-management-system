package com.example.contact.infrastructure.storage;

import com.example.contact.domain.port.AvatarStoragePort;
import com.example.contact.domain.valueobject.ContactId;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.ByteArrayInputStream;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.avatar.provider", havingValue = "minio", matchIfMissing = true)
public class MinioAvatarStorageAdapter implements AvatarStoragePort {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioAvatarStorageAdapter(
            @Value("${app.avatar.minio.endpoint}") String endpoint,
            @Value("${app.avatar.minio.access-key}") String accessKey,
            @Value("${app.avatar.minio.secret-key}") String secretKey,
            @Value("${app.avatar.minio.bucket:avatars}") String bucket) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucket = bucket;
    }

    @Override
    public String store(ContactId contactId, String contentType, byte[] data) {
        String safeContentType = contentType == null || contentType.isBlank() ? "application/octet-stream" : contentType;
        String extension = extensionFromContentType(contentType);
        String objectKey = "avatars/" + contactId.value() + extension;
        try {
            ensureBucket();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectKey)
                    .stream(new ByteArrayInputStream(data), data.length, -1)
                    .contentType(safeContentType)
                    .build());
            return "minio://" + bucket + "/" + objectKey;
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to store avatar in MinIO", exception);
        }
    }

    private void ensureBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to initialize MinIO bucket", exception);
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
