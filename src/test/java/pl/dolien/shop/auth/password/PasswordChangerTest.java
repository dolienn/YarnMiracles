package pl.dolien.shop.auth.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.exception.IncorrectPasswordException;
import pl.dolien.shop.exception.SamePasswordException;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordChangerTest {

    private static final String INCORRECT_PASSWORD_MESSAGE = "The current password does not match " +
                                                             "the one entered in the form";
    private static final String SAME_PASSWORD_MESSAGE = "The current password and the new one are the same";
    private static final String CURRENT_PASSWORD = "password123";
    private static final String NEW_PASSWORD = "newPassword123";

    @InjectMocks
    private PasswordChanger passwordChanger;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    private UserDTO testUserDTO;
    private User testUser;
    private PasswordRequestDTO testPasswordRequestDTO;
    private PasswordRequestDTO testPasswordRequestDTOWithSamePasswords;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldChangePassword() {
        mockUserServiceAndPasswordEncoder(true);
        when(passwordEncoder.encode(anyString())).thenReturn(NEW_PASSWORD);
        when(userService.saveUser(any(User.class))).thenReturn(testUser);

        User response = passwordChanger.changePassword(testPasswordRequestDTO, authentication);

        assertEquals(testUser.getPassword(), response.getPassword());
        assertEquals(NEW_PASSWORD, testUser.getPassword());

        verifyUserServiceAndPasswordEncoderInteractions();
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userService, times(1)).saveUser(testUser);
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        mockUserServiceAndPasswordEncoder(false);

        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class,
                () -> passwordChanger.changePassword(testPasswordRequestDTO, authentication)
        );

        assertEquals(INCORRECT_PASSWORD_MESSAGE, exception.getMessage());

        verifyUserServiceAndPasswordEncoderInteractions();
    }

    @Test
    void shouldThrowExceptionWhenPasswordsAreTheSame() {
        mockUserServiceAndPasswordEncoder(true);

        SamePasswordException exception = assertThrows(SamePasswordException.class,
                () -> passwordChanger.changePassword(testPasswordRequestDTOWithSamePasswords, authentication)
        );

        assertEquals(SAME_PASSWORD_MESSAGE, exception.getMessage());

        verifyUserServiceAndPasswordEncoderInteractions();
    }

    private void initializeTestData() {
        testUserDTO = UserDTO.builder()
                .id(1)
                .build();

        testUser = User.builder()
                .id(1)
                .password(CURRENT_PASSWORD)
                .build();

        testPasswordRequestDTO = PasswordRequestDTO.builder()
                .yourPassword(CURRENT_PASSWORD)
                .newPassword(NEW_PASSWORD)
                .build();

        testPasswordRequestDTOWithSamePasswords = PasswordRequestDTO.builder()
                .yourPassword(CURRENT_PASSWORD)
                .newPassword(CURRENT_PASSWORD)
                .build();
    }

    private void mockUserServiceAndPasswordEncoder(boolean passwordMatches) {
        when(userService.getUserDTOByAuth(authentication)).thenReturn(testUserDTO);
        when(userService.getUserById(testUserDTO.getId())).thenReturn(testUser);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(passwordMatches);
    }

    private void verifyUserServiceAndPasswordEncoderInteractions() {
        verify(userService, times(1)).getUserDTOByAuth(authentication);
        verify(userService, times(1)).getUserById(testUserDTO.getId());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }
}