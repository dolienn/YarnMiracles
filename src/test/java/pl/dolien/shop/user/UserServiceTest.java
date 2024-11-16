package pl.dolien.shop.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.exception.EmailAlreadyExistsException;
import pl.dolien.shop.exception.UserNotFoundException;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.user.dto.UserDTO;
import pl.dolien.shop.user.dto.UserRequestDTO;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String NEW_EMAIL = "newEmail@example.com";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private static final String AUTH_USER_NOT_FOUND_MESSAGE = "Authenticated user not found";
    private static final String ACCESS_DENIED_MESSAGE = "You don't have permission to perform this action";
    private static final String AUTH_USER_DOES_NOT_MATCH_REQUESTED_USER_MESSAGE = "Authenticated user does not match the requested user";
    private static final String EMAIL_ALREADY_EXISTS_MESSAGE = "User with email " + USER_EMAIL + " already exists";

    @InjectMocks
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private Role adminRole;
    private User testUser;
    private User testAdmin;
    private UserRequestDTO testUserRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldReturnUserWhenUserIdExists() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUserById(testUser.getId());

        assertUser(foundUser);
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserIdNotFound() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(testUser.getId())
        );

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userRepository, times(1)).findById(testUser.getId());
    }

    @Test
    void shouldReturnUserWhenUserEmailExists() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        User foundUser = userService.getUserByEmail(USER_EMAIL);

        assertUser(foundUser);
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
    }

    @Test
    void shouldThrowExceptionWhenUserEmailNotFound() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByEmail(USER_EMAIL)
        );

        assertEquals(USER_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(USER_EMAIL);
    }

    @Test
    void shouldReturnUserDTOWhenAuthenticationIsValid() {
        when(authentication.getPrincipal()).thenReturn(testUser);

        UserDTO foundUserDTO = userService.getUserDTOByAuth(authentication);

        assertUserDTO(testUser, foundUserDTO);
        verify(authentication, times(1)).getPrincipal();
    }

    @Test
    void shouldThrowExceptionWhenAuthenticationIsNull() {
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserDTOByAuth(null)
        );

        assertEquals(AUTH_USER_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldSaveUser() {
        when(userRepository.save(testUser)).thenReturn(testUser);

        User savedUser = userService.saveUser(testUser);

        assertUser(savedUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void shouldAddRoleToUserWhenConnectedUserHasAdminRole() throws RoleNotFoundException {
        mockAuthenticationAndUserRepository(testAdmin);
        mockRoleAndUserDependencies();

        UserWithRoleDTO response = userService.addRole(USER_EMAIL, adminRole.getName(), authentication);

        assertUserWithRoleDTO(response);

        verifyAuthenticationAndUserRepository(testAdmin, 1);
        verifyRoleAndUserDependencies();
    }

    @Test
    void shouldThrowExceptionWhenConnectedUserDoesNotHaveAdminRole() {
        mockAuthenticationAndUserRepository(testUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> userService.addRole(USER_EMAIL, adminRole.getName(), authentication)
        );

        assertEquals(ACCESS_DENIED_MESSAGE, exception.getMessage());

        verifyAuthenticationAndUserRepository(testUser, 1);
    }

    @Test
    void shouldEditUser() {
        mockAuthenticationAndUserRepository(testAdmin);

        when(userRepository.findByEmail(testUserRequestDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(testAdmin)).thenReturn(testAdmin);

        UserDTO response = userService.editUser(testUserRequestDTO, authentication);

        assertUserDTO(testAdmin, response);

        verifyAuthenticationAndUserRepository(testAdmin, 2);
        verifyUserRepository();
    }

    @Test
    void shouldAllowAccessWhenUserHasAdminRole() {
        mockAuthenticationAndUserRepository(testAdmin);

        assertDoesNotThrow(() -> userService.verifyUserHasAdminRole(authentication));

        verifyAuthenticationAndUserRepository(testAdmin, 1);
    }

    @Test
    void shouldDenyAccessWhenUserDoesNotHaveAdminRole() {
        mockAuthenticationAndUserRepository(testUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> userService.verifyUserHasAdminRole(authentication)
        );

        assertEquals(ACCESS_DENIED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldAllowAccessWhenAuthenticatedUserMatchesRequestedUser() {
        when(authentication.getPrincipal()).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.verifyUserIsAuthenticatedUser(testUser.getId(), authentication));
    }

    @Test
    void shouldDenyAccessWhenAuthenticatedUserDoesNotMatchRequestedUser() {
        when(authentication.getPrincipal()).thenReturn(testUser);

        AccessDeniedException exception = assertThrows(
                AccessDeniedException.class,
                () -> userService.verifyUserIsAuthenticatedUser(testAdmin.getId(), authentication)
        );

        assertEquals(AUTH_USER_DOES_NOT_MATCH_REQUESTED_USER_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsNotTaken() {
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userService.assertEmailNotInUse(NEW_EMAIL, USER_EMAIL));
    }

    @Test
    void shouldNotThrowExceptionWhenEmailIsSameAsCurrentEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        assertDoesNotThrow(() -> userService.assertEmailNotInUse(USER_EMAIL, USER_EMAIL));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsTakenByAnotherUser() {
        when(userRepository.findByEmail(ADMIN_EMAIL)).thenReturn(Optional.of(testAdmin));

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.assertEmailNotInUse(ADMIN_EMAIL, USER_EMAIL)
        );

        assertEquals(EMAIL_ALREADY_EXISTS_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenEmailIsTakenByAnotherUser() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        assertTrue(userService.isEmailTaken(USER_EMAIL, ADMIN_EMAIL));
    }

    @Test
    void shouldReturnFalseWhenEmailIsSameAsCurrentUserEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        assertFalse(userService.isEmailTaken(USER_EMAIL, USER_EMAIL));
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExistInDatabase() {
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());

        assertFalse(userService.isEmailTaken(NEW_EMAIL, testUser.getEmail()));
    }

    @Test
    void shouldReturnTrueWhenUserExistsWithEmail() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(testUser));

        assertTrue(userService.isUserExists(USER_EMAIL));
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistWithEmail() {
        when(userRepository.findByEmail(NEW_EMAIL)).thenReturn(Optional.empty());

        assertFalse(userService.isUserExists(NEW_EMAIL));
    }

    private void initializeTestData() {
        Role userRole = Role.builder().id(1).name("USER").build();

        adminRole = Role.builder().id(2).name("ADMIN").build();

        testUser = User.builder()
                .id(1)
                .email(USER_EMAIL)
                .roles(new HashSet<>(Set.of(userRole)))
                .build();

        testAdmin = User.builder()
                .id(2)
                .email(ADMIN_EMAIL)
                .roles(new HashSet<>(Set.of(userRole, adminRole)))
                .build();

        testUserRequestDTO = UserRequestDTO.builder()
                .id(2)
                .email("editedAdmin@example.com")
                .build();
    }

    private void mockAuthenticationAndUserRepository(User user) {
        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    private void mockRoleAndUserDependencies() throws RoleNotFoundException {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.ofNullable(testUser));
        when(roleService.getByName(adminRole.getName())).thenReturn(adminRole);
        when(userRepository.save(testUser)).thenReturn(testUser);
    }

    private void assertUser(User user) {
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
        assertEquals(USER_EMAIL, user.getEmail());
    }

    private void assertUserDTO(User user, UserDTO userDTO) {
        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    private void assertUserWithRoleDTO(UserWithRoleDTO userWithRoleDTO) {
        assertNotNull(userWithRoleDTO);
        assertEquals(testUser.getId(), userWithRoleDTO.getId());
        assertEquals(USER_EMAIL, userWithRoleDTO.getEmail());
        assertEquals(2, userWithRoleDTO.getRoles().size());
    }

    private void verifyAuthenticationAndUserRepository(User user, int byIdTimes) {
        verify(authentication, times(1)).getPrincipal();
        verify(userRepository, times(byIdTimes)).findById(user.getId());
    }

    private void verifyRoleAndUserDependencies() throws RoleNotFoundException {
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
        verify(roleService, times(1)).getByName(adminRole.getName());
        verify(userRepository, times(1)).save(testUser);
    }

    private void verifyUserRepository() {
        verify(userRepository, times(1)).findByEmail(testUserRequestDTO.getEmail());
        verify(userRepository, times(1)).save(testAdmin);
    }
}