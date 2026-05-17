package com.example.contact.interfaces.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.contact.application.command.ContactResult;
import com.example.contact.application.port.in.CreateContactUseCase;
import com.example.contact.application.port.in.DeleteContactUseCase;
import com.example.contact.application.port.in.GetContactUseCase;
import com.example.contact.application.port.in.ListContactsUseCase;
import com.example.contact.application.port.in.UpdateContactUseCase;
import com.example.contact.domain.valueobject.ContactId;
import com.example.contact.interfaces.rest.exception.ContactExceptionHandler;
import com.example.contact.interfaces.rest.mapper.ContactRestMapperImpl;
import com.example.contact.shared.mapper.DomainTypeMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ContactController.class)
@Import({ContactExceptionHandler.class, ContactRestMapperImpl.class, DomainTypeMapper.class})
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

    @Test
    void list_returns200() throws Exception {
        ContactId id = ContactId.generate();
        when(listContactsUseCase.execute())
                .thenReturn(List.of(ContactResult.builder()
                        .id(id)
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane@example.com")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()));

        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Jane"));
    }

    @Test
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
