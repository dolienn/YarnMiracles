package pl.dolien.shop.auth.userProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.auth.password.PasswordChanger;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserDTO;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileUpdaterTest {

    private static final String USER_NEW_EMAIL = "newTest@example.com";

    @InjectMocks
    private UserProfileUpdater userProfileUpdater;

    @Mock
    private UserService userService;

    @Mock
    private PasswordChanger passwordChanger;

    @Mock
    private Authentication authentication;

    private UserDTO testUserDTO;
    private User testUser;
    private RegistrationDTO testRegistrationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldUpdateUserProfile() {
        mockDependenciesForUpdateProfile();

        User response = userProfileUpdater.updateUserProfile(testRegistrationDTO, authentication);

        assertUpdatedUserProfile(response);
        verifyInteractionsForUpdateProfile();
    }

    private void initializeTestData() {
        testUserDTO = UserDTO.builder()
                .id(1)
                .build();

        testUser = User.builder()
                .id(1)
                .email("test@example.com")
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .dateOfBirth(parse("2020-01-01"))
                .build();

        testRegistrationDTO = RegistrationDTO.builder()
                .email(USER_NEW_EMAIL)
                .firstname("NewJohn")
                .lastname("NewDoe")
                .password("newPassword123")
                .dateOfBirth(parse("2020-01-01"))
                .build();
    }

    private void mockDependenciesForUpdateProfile() {
        when(userService.getUserDTOByAuth(authentication)).thenReturn(testUserDTO);
        when(userService.getUserById(testUserDTO.getId())).thenReturn(testUser);
        when(userService.saveUser(testUser)).thenReturn(testUser);
    }

    private void assertUpdatedUserProfile(User response) {
        assertNotNull(response);
        assertEquals(testUser.getFirstname(), response.getFirstname());
        assertEquals(testUser.getLastname(), response.getLastname());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(USER_NEW_EMAIL, testUser.getEmail());
    }

    private void verifyInteractionsForUpdateProfile() {
        verify(userService, times(1)).getUserDTOByAuth(authentication);
        verify(userService, times(1)).getUserById(testUserDTO.getId());
        verify(userService, times(1)).saveUser(testUser);
    }
}