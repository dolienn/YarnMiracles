package pl.dolien.shop.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
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

    private Role adminRole;
    private UserDTO testUserDTO;
    private UserRequestDTO testUserRequestDTO;
    private UserWithRoleDTO testUserWithRoleDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        setupRolesAndUsers();
    }

    @Test
    void shouldReturnUserDTOWhenAuthenticationIsValid() throws Exception {
        when(userService.getUserDTOByAuth(authentication)).thenReturn(testUserDTO);

        mockMvc.perform(get("/users/byAuth")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserDTO)));

        verify(userService, times(1)).getUserDTOByAuth(authentication);
    }

    @Test
    void shouldEditUser() throws Exception {
        when(userService.editUser(any(UserRequestDTO.class), eq(authentication))).thenReturn(testUserDTO);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserRequestDTO))
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserDTO)));

        verify(userService, times(1)).editUser(any(UserRequestDTO.class), eq(authentication));
    }

    @Test
    void shouldAddRoleToUser() throws Exception {
        when(userService.addRole(USER_EMAIL, ADMIN_ROLE, authentication)).thenReturn(testUserWithRoleDTO);

        mockMvc.perform(get("/users/{email}/roles/{roleName}", USER_EMAIL, ADMIN_ROLE)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserWithRoleDTO)));

        verify(userService, times(1)).addRole(USER_EMAIL, ADMIN_ROLE, authentication);
    }

    private void setupRolesAndUsers() {
        adminRole = Role.builder().id(2).name(ADMIN_ROLE).build();

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
    }
}
