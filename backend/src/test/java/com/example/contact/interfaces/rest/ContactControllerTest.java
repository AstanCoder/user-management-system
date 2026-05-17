package com.example.contact.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.contact.application.command.ContactPageResult;
import com.example.contact.application.command.ContactResult;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.application.port.in.UploadAvatarUseCase;
import com.example.shared.test.WebMvcTestSecurityConfig;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.exception.ContactExceptionHandler;
import com.example.contact.interfaces.rest.mapper.ContactRestMapperImpl;
import com.example.contact.shared.mapper.DomainTypeMapper;
import com.example.identity.infrastructure.config.SecurityConfig;
import com.example.identity.infrastructure.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
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
@Import({ContactExceptionHandler.class, ContactRestMapperImpl.class, DomainTypeMapper.class, WebMvcTestSecurityConfig.class})
class ContactControllerTest {

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

    @Test
    void list_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/contacts")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "VIEWER")
    void list_returns200() throws Exception {
        ContactId id = ContactId.generate();
        when(listContactsUseCase.execute(any()))
                .thenReturn(ContactPageResult.builder()
                        .content(List.of(ContactResult.builder()
                                .id(id)
                                .firstName("Jane")
                                .lastName("Doe")
                                .email("jane@example.com")
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()))
                        .totalElements(1)
                        .totalPages(1)
                        .page(0)
                        .size(20)
                        .build());

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Jane"));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void create_invalidEmail_returns400() throws Exception {
        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "firstName", "Jane",
                                "lastName", "Doe",
                                "email", "invalid"))))
                .andExpect(status().isBadRequest());
    }
}
