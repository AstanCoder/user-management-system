package com.example.contact.infrastructure.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ImgproxyAvatarUrlResolverAdapterTest {

    @Test
    void resolve_relativeAvatar_returnsAbsoluteBackendUrl() {
        var resolver = new ImgproxyAvatarUrlResolverAdapter(
                "http://localhost:8080",
                "http://localhost:8082",
                "avatars",
                "0123456789abcdef0123456789abcdef",
                "abcdef0123456789abcdef0123456789");

        String resolved = resolver.resolve("/avatars/contact.jpg");

        assertEquals("http://localhost:8080/avatars/contact.jpg", resolved);
    }

    @Test
    void resolve_minioAvatar_returnsSignedImgproxyUrl() {
        var resolver = new ImgproxyAvatarUrlResolverAdapter(
                "http://localhost:8080",
                "http://localhost:8082",
                "avatars",
                "0123456789abcdef0123456789abcdef",
                "abcdef0123456789abcdef0123456789");

        String resolved = resolver.resolve("minio://avatars/avatars/contact.jpg");

        assertTrue(resolved.startsWith("http://localhost:8082/"));
        assertTrue(resolved.contains("/rs:fill:256:256:0/"));
    }
}
