package pl.dolien.shop.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.management.relation.RoleNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    private static final String USER_ROLE_NAME = "USER";
    private static final String ROLE_NOT_FOUND_MESSAGE = "Role USER not found";

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    private Role userRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldGetRoleByName() throws RoleNotFoundException {
        when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(userRole));

        Role result = roleService.getByName(USER_ROLE_NAME);

        assertEquals(userRole, result);

        verify(roleRepository, times(1)).findByName(USER_ROLE_NAME);
    }

    @Test
    void shouldThrowRoleNotFoundException() {
        when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> roleService.getByName(USER_ROLE_NAME));

        assertEquals(ROLE_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    private void initializeTestData() {
        userRole = Role.builder()
                .id(1)
                .name(USER_ROLE_NAME)
                .build();
    }
}
