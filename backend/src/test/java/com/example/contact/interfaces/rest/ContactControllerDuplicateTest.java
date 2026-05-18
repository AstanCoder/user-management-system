package com.example.contact.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.application.port.in.UploadAvatarUseCase;
import com.example.contact.application.port.out.ResolveAvatarUrlPort;
import com.example.shared.test.WebMvcTestSecurityConfig;
import com.example.contact.domain.exception.DuplicateEmailException;
import com.example.contact.domain.valueobject.Email;
import com.example.contact.interfaces.rest.exception.ContactExceptionHandler;
import com.example.contact.interfaces.rest.mapper.AvatarUrlRestMapper;
import com.example.contact.interfaces.rest.mapper.ContactRestMapperImpl;
import com.example.contact.shared.mapper.DomainTypeMapper;
import com.example.identity.infrastructure.config.SecurityConfig;
import com.example.identity.infrastructure.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = ContactController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {JwtAuthenticationFilter.class, SecurityConfig.class}))
@Import({
    ContactExceptionHandler.class,
    AvatarUrlRestMapper.class,
    ContactRestMapperImpl.class,
    DomainTypeMapper.class,
    WebMvcTestSecurityConfig.class
})
class ContactControllerDuplicateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ListContactsUseCase listContactsUseCase;

    @MockBean
    private GetContactUseCase getContactUseCase;

    @MockBean
    private CreateContactUseCase createContactUseCase;

    @MockBean
    private UpdateContactUseCase updateContactUseCase;

    @MockBean
    private DeleteContactUseCase deleteContactUseCase;

    @MockBean
    private UploadAvatarUseCase uploadAvatarUseCase;

    @MockBean
    private ResolveAvatarUrlPort resolveAvatarUrlPort;

    @Test
    @WithMockUser(roles = "EDITOR")
    void create_duplicateEmail_returns409() throws Exception {
        when(createContactUseCase.execute(any()))
                .thenThrow(new DuplicateEmailException(Email.create("dup@example.com")));

        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", "Jane",
                                "lastName", "Doe",
                                "email", "dup@example.com"))))
                .andExpect(status().isConflict());
    }
}
