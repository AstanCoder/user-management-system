package com.example.user.interfaces.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.shared.test.WebMvcTestSecurityConfig;
import com.example.identity.infrastructure.config.SecurityConfig;
import com.example.identity.infrastructure.security.JwtAuthenticationFilter;
import com.example.user.application.command.UserPageResult;
import com.example.user.application.command.UserSearchQuery;
import com.example.user.application.command.UserStatsResult;
import com.example.user.application.port.in.CreateUserUseCase;
import com.example.user.application.port.in.DeleteUserUseCase;
import com.example.user.application.port.in.GetUserStatsUseCase;
import com.example.user.application.port.in.InviteUserUseCase;
import com.example.user.application.port.in.ListUsersUseCase;
import com.example.user.application.port.in.UpdateUserUseCase;
import com.example.user.interfaces.rest.exception.UserExceptionHandler;
import com.example.user.interfaces.rest.mapper.UserRestMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {JwtAuthenticationFilter.class, SecurityConfig.class}))
@Import({UserExceptionHandler.class, UserRestMapperImpl.class, WebMvcTestSecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListUsersUseCase listUsersUseCase;

    @MockBean
    private GetUserStatsUseCase getUserStatsUseCase;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private InviteUserUseCase inviteUserUseCase;

    @Test
    void stats_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/users/stats")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void stats_asEditor_returns403() throws Exception {
        mockMvc.perform(get("/api/users/stats")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void stats_asAdmin_returns200() throws Exception {
        when(getUserStatsUseCase.execute())
                .thenReturn(UserStatsResult.builder()
                        .totalUsers(3)
                        .activeUsers(2)
                        .invitedUsers(1)
                        .disabledUsers(0)
                        .adminCount(1)
                        .editorCount(1)
                        .viewerCount(1)
                        .invitedPendingCount(1)
                        .usersCreatedLast7Days(2)
                        .build());
        when(listUsersUseCase.execute(org.mockito.ArgumentMatchers.any(UserSearchQuery.class)))
                .thenReturn(UserPageResult.builder()
                        .content(java.util.List.of())
                        .totalElements(0)
                        .totalPages(0)
                        .page(0)
                        .size(20)
                        .build());

        mockMvc.perform(get("/api/users/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsers").value(3))
                .andExpect(jsonPath("$.activeUsers").value(2));
    }
}
