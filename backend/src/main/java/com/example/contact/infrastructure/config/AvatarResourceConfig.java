package com.example.contact.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves uploaded avatar files from local storage.
 */
@Configuration
public class AvatarResourceConfig implements WebMvcConfigurer {

    private final String storagePath;

    public AvatarResourceConfig(@Value("${app.avatar.storage-path:./data/avatars}") String storagePath) {
        this.storagePath = storagePath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + storagePath + "/");
    }
}
