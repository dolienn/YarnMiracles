package pl.dolien.shop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.dolien.shop.role.RoleMapper.toRoleDTO;

public class UserControllerTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String ADMIN_ROLE = "ADMIN";

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private UserDTO testUserDTO;
    private UserRequestDTO testUserRequestDTO;
    private UserWithRoleDTO testUserWithRoleDTO;
    private PaginationAndSortParams testPaginationAndSortParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        initializeTestData();
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userService.getAllUsers(any(PaginationAndSortParams.class), any(Authentication.class)))
                .thenReturn(buildRestPage(List.of(testUserWithRoleDTO)));

        performGetAllUsers(testPaginationAndSortParams, authentication)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buildRestPage(List.of(testUserWithRoleDTO)))));

        verify(userService, times(1))
                .getAllUsers(any(PaginationAndSortParams.class), any(Authentication.class));
    }

    @Test
    void shouldReturnUserWithRolesById() throws Exception {
        when(userService.getUserWithRolesById(anyInt(), any(Authentication.class))).thenReturn(testUserWithRoleDTO);

        performGetUserWithRolesById(testUserWithRoleDTO.getId(), authentication)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserWithRoleDTO)));

        verify(userService, times(1)).getUserWithRolesById(anyInt(), any(Authentication.class));
    }

    @Test
    void shouldReturnUserDTOWhenAuthenticationIsValid() throws Exception {
        when(userService.getUserByAuth(authentication)).thenReturn(testUserWithRoleDTO);

        performGetUserDTOByAuth(authentication)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserWithRoleDTO)));

        verify(userService, times(1)).getUserByAuth(authentication);
    }

    @Test
    void shouldEditUser() throws Exception {
        when(userService.editUser(any(UserRequestDTO.class), eq(authentication))).thenReturn(testUserDTO);

        performEditUser(testUserRequestDTO, authentication)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserDTO)));

        verify(userService, times(1)).editUser(any(UserRequestDTO.class), eq(authentication));
    }

    @Test
    void shouldAddRoleToUser() throws Exception {
        when(userService.addRole(USER_EMAIL, ADMIN_ROLE, authentication)).thenReturn(testUserWithRoleDTO);

        performAddRoleToUser(authentication)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserWithRoleDTO)));

        verify(userService, times(1)).addRole(USER_EMAIL, ADMIN_ROLE, authentication);
    }

    @Test
    void shouldRemoveRoleFromUser() throws Exception {
        performRemoveRoleFromUser(authentication)
                .andExpect(status().isOk());

        verify(userService, times(1)).removeRole(USER_EMAIL, ADMIN_ROLE, authentication);
    }

    private void initializeTestData() {
        Role adminRole = Role.builder().id(2).name(ADMIN_ROLE).build();

        testUserDTO = UserDTO.builder()
                .id(1)
                .email(USER_EMAIL)
                .build();

        testUserRequestDTO = UserRequestDTO.builder()
                .id(2)
                .email("editedUser@example.com")
                .build();

        testUserWithRoleDTO = UserWithRoleDTO.builder()
                .id(1)
                .email(USER_EMAIL)
                .roles(Set.of(toRoleDTO(adminRole)))
                .build();

        testPaginationAndSortParams = PaginationAndSortParams.builder()
                .page(0)
                .size(10)
                .sortBy("PRICE_ASC")
                .build();
    }

    private ResultActions performGetAllUsers(PaginationAndSortParams testPaginationAndSortParams,
                                             Authentication authentication) throws Exception {
        return mockMvc.perform(get("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams))
                .principal(authentication));
    }

    private ResultActions performGetUserWithRolesById(Integer id, Authentication authentication) throws Exception {
        return mockMvc.perform(get("/users/{id}", id)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(id))
                .principal(authentication));
    }

    private ResultActions performGetUserDTOByAuth(Authentication authentication) throws Exception {
        return mockMvc.perform(get("/users/byAuth")
                .principal(authentication));
    }

    private ResultActions performEditUser(UserRequestDTO userRequestDTO, Authentication authentication) throws Exception {
        return mockMvc.perform(put("/users")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDTO))
                .principal(authentication));
    }

    private ResultActions performAddRoleToUser(Authentication authentication) throws Exception {
        return mockMvc.perform(get("/users/{email}/roles/{roleName}", USER_EMAIL, ADMIN_ROLE)
                .principal(authentication));
    }

    private ResultActions performRemoveRoleFromUser(Authentication authentication) throws Exception {
        return mockMvc.perform(delete("/users/{email}/roles/{roleName}", USER_EMAIL, ADMIN_ROLE)
                .principal(authentication));
    }

    private Page<UserWithRoleDTO> buildRestPage(List<UserWithRoleDTO> users) {
        return new RestPage<>(users, 1, 1, 1);
    }
}
