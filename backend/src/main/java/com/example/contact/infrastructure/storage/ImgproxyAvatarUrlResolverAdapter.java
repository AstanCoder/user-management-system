package com.example.contact.infrastructure.storage;

import com.example.contact.application.port.out.ResolveAvatarUrlPort;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImgproxyAvatarUrlResolverAdapter implements ResolveAvatarUrlPort {

    private final String backendPublicBaseUrl;
    private final String imgproxyBaseUrl;
    private final String minioBucket;
    private final byte[] imgproxyKey;
    private final byte[] imgproxySalt;

    public ImgproxyAvatarUrlResolverAdapter(
            @Value("${app.avatar.public-base-url:http://localhost:8080}") String backendPublicBaseUrl,
            @Value("${app.imgproxy.base-url:http://localhost:8082}") String imgproxyBaseUrl,
            @Value("${app.avatar.minio.bucket:avatars}") String minioBucket,
            @Value("${app.imgproxy.key:0123456789abcdef0123456789abcdef}") String imgproxyKeyHex,
            @Value("${app.imgproxy.salt:abcdef0123456789abcdef0123456789}") String imgproxySaltHex) {
        this.backendPublicBaseUrl = stripTrailingSlash(backendPublicBaseUrl);
        this.imgproxyBaseUrl = stripTrailingSlash(imgproxyBaseUrl);
        this.minioBucket = minioBucket;
        this.imgproxyKey = hexToBytes(imgproxyKeyHex);
        this.imgproxySalt = hexToBytes(imgproxySaltHex);
    }

    @Override
    public String resolve(String storedAvatarUrl) {
        if (storedAvatarUrl == null || storedAvatarUrl.isBlank()) {
            return storedAvatarUrl;
        }
        if (storedAvatarUrl.startsWith("http://") || storedAvatarUrl.startsWith("https://")) {
            return storedAvatarUrl;
        }
        if (storedAvatarUrl.startsWith("/avatars/")) {
            return backendPublicBaseUrl + storedAvatarUrl;
        }
        if (!storedAvatarUrl.startsWith("minio://")) {
            return storedAvatarUrl;
        }
        String objectKey = extractObjectKey(storedAvatarUrl);
        String source = "s3://" + minioBucket + "/" + objectKey;
        String encodedSource = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(source.getBytes(StandardCharsets.UTF_8));
        String path = "/rs:fill:256:256:0/" + encodedSource + ".webp";
        String signature = sign(path);
        return imgproxyBaseUrl + "/" + signature + path;
    }

    private String sign(String path) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(imgproxyKey, "HmacSHA256"));
            mac.update(imgproxySalt);
            byte[] digest = mac.doFinal(path.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to sign imgproxy url", ex);
        }
    }

    private String extractObjectKey(String storedAvatarUrl) {
        String prefix = "minio://" + minioBucket + "/";
        if (storedAvatarUrl.startsWith(prefix)) {
            return storedAvatarUrl.substring(prefix.length());
        }
        return storedAvatarUrl.substring("minio://".length());
    }

    private byte[] hexToBytes(String value) {
        int length = value.length();
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex value length");
        }
        byte[] bytes = new byte[length / 2];
        for (int index = 0; index < length; index += 2) {
            bytes[index / 2] = (byte) Integer.parseInt(value.substring(index, index + 2), 16);
        }
        return bytes;
    }

    private String stripTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
