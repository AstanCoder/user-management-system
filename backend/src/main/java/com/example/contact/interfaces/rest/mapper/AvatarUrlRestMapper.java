package com.example.contact.interfaces.rest.mapper;

import com.example.contact.application.port.out.ResolveAvatarUrlPort;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class AvatarUrlRestMapper {

    private final ResolveAvatarUrlPort resolveAvatarUrlPort;

    public AvatarUrlRestMapper(ResolveAvatarUrlPort resolveAvatarUrlPort) {
        this.resolveAvatarUrlPort = resolveAvatarUrlPort;
    }

    @Named("resolveAvatarUrl")
    public String resolveAvatarUrl(String avatarUrl) {
        return resolveAvatarUrlPort.resolve(avatarUrl);
    }
}
