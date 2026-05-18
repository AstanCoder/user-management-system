package com.example.contact.interfaces.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.contact.application.command.ActivityResult;
import com.example.contact.application.command.TagResult;
import com.example.contact.application.port.in.AddNoteUseCase;
import com.example.contact.application.port.in.AssignTagsUseCase;
import com.example.contact.application.port.in.ConfirmActivityUseCase;
import com.example.contact.application.port.in.DeleteActivityUseCase;
import com.example.contact.application.port.in.DeleteNoteUseCase;
import com.example.contact.application.port.in.ListActivitiesUseCase;
import com.example.contact.application.port.in.ListNotesUseCase;
import com.example.contact.application.port.in.LogActivityUseCase;
import com.example.contact.infrastructure.config.CorsConfig;
import com.example.contact.interfaces.rest.exception.ContactExceptionHandler;
import com.example.identity.infrastructure.config.SecurityConfig;
import com.example.identity.infrastructure.security.AuthenticatedUserPrincipal;
import com.example.identity.infrastructure.security.JwtAuthenticationFilter;
import com.example.user.domain.model.Role;
import com.example.user.domain.model.UserId;
import com.example.shared.test.WebMvcTestSecurityConfig;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = {ContactActivitiesController.class, ContactNotesController.class, ContactTagsController.class},
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {JwtAuthenticationFilter.class, SecurityConfig.class}))
@Import({ContactExceptionHandler.class, CorsConfig.class, WebMvcTestSecurityConfig.class})
class ContactInteractionControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogActivityUseCase logActivityUseCase;

    @MockBean
    private ListActivitiesUseCase listActivitiesUseCase;

    @MockBean
    private ConfirmActivityUseCase confirmActivityUseCase;

    @MockBean
    private DeleteActivityUseCase deleteActivityUseCase;

    @MockBean
    private AddNoteUseCase addNoteUseCase;

    @MockBean
    private ListNotesUseCase listNotesUseCase;

    @MockBean
    private DeleteNoteUseCase deleteNoteUseCase;

    @MockBean
    private AssignTagsUseCase assignTagsUseCase;

    @Test
    @WithMockUser(roles = "ADMIN")
    void logActivity_withoutConfirmedFlag_defaultsToConfirmed() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        when(logActivityUseCase.execute(
                        any(),
                        any(),
                        eq("NOTE"),
                        eq("meeting"),
                        any(),
                        eq(true)))
                .thenReturn(ActivityResult.builder()
                        .id(UUID.randomUUID().toString())
                        .activityType("NOTE")
                        .description("meeting")
                        .authorUserId(UUID.randomUUID())
                        .occurredAt(Instant.now())
                        .createdAt(Instant.now())
                        .confirmed(true)
                        .build());

        mockMvc.perform(post("/api/contacts/{contactId}/activities", contactId)
                        .with(authentication(adminAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activityType\":\"NOTE\",\"description\":\"meeting\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.confirmed").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmActivity_returnsUpdatedActivity() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        String activityId = UUID.randomUUID().toString();
        when(confirmActivityUseCase.execute(any(), any()))
                .thenReturn(ActivityResult.builder()
                        .id(activityId)
                        .activityType("EMAIL")
                        .description("Email started")
                        .authorUserId(UUID.randomUUID())
                        .occurredAt(Instant.now())
                        .createdAt(Instant.now())
                        .confirmed(true)
                        .build());

        mockMvc.perform(patch("/api/contacts/{contactId}/activities/{activityId}/confirm", contactId, activityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(activityId))
                .andExpect(jsonPath("$.confirmed").value(true));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void confirmActivity_preflight_allowsPatchMethod() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        String activityId = UUID.randomUUID().toString();

        mockMvc.perform(options("/api/contacts/{contactId}/activities/{activityId}/confirm", contactId, activityId)
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "PATCH"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", Matchers.containsString("PATCH")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUnconfirmedActivity_returnsNoContent() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        String activityId = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/contacts/{contactId}/activities/{activityId}", contactId, activityId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteNote_returnsNoContent() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        String noteId = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/contacts/{contactId}/notes/{noteId}", contactId, noteId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignTags_deserializesTagNamesPayload() throws Exception {
        String contactId = "fcd23632-d13c-45ab-8d1e-a2fbfd207ef6";
        when(assignTagsUseCase.execute(any(), any()))
                .thenReturn(List.of(TagResult.builder()
                        .id(UUID.randomUUID().toString())
                        .name("typescript")
                        .build()));

        mockMvc.perform(put("/api/contacts/{contactId}/tags", contactId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tagNames\":[\"typescript\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("typescript"));
    }

    private UsernamePasswordAuthenticationToken adminAuthentication() {
        AuthenticatedUserPrincipal principal = new AuthenticatedUserPrincipal(
                UserId.of("00000000-0000-0000-0000-000000000001"),
                "admin@nexuscrm.com",
                Role.ADMIN);
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }
}
