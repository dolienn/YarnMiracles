package pl.dolien.shop.auth;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController controller;

    @Mock
    private AuthenticationService service;

    @Mock
    private Authentication auth;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegister() throws MessagingException {
        ResponseEntity<?> response = controller.register(
                RegistrationRequest.builder()
                        .firstname("John")
                        .lastname("Doe")
                        .email("john.doe@example.com")
                        .password("password123")
                        .build()
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        verify(service, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    public void shouldChangeAccountDetails() {
        ResponseEntity<?> response = controller.changeAccountDetails(
                RegistrationRequest.builder()
                        .firstname("John")
                        .lastname("Doe")
                        .email("john.doe@example.com")
                        .password("password123")
                        .build(),
                auth
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        verify(service, times(1)).changeAccountDetails(any(RegistrationRequest.class), any(Authentication.class));
    }

    @Test
    public void shouldChangePassword() {
        ResponseEntity<?> response = controller.changePassword(
                PasswordRequest.builder()
                        .yourPassword("password123")
                        .newPassword("newPassword123")
                        .build(),
                auth
        );
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        verify(service, times(1)).changePassword(any(PasswordRequest.class), any(Authentication.class));
    }

    @Test
    public void shouldAuthenticate() {
        ResponseEntity<AuthenticationResponse> response = controller.authenticate(
                AuthenticationRequest.builder()
                        .email("john.doe@example.com")
                        .password("password123")
                        .build()
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(service, times(1)).authenticate(any(AuthenticationRequest.class));
    }

    @Test
    public void shouldActivateAccount() throws MessagingException {
        controller.confirm(
                "token");

        verify(service, times(1)).activateAccount(any(String.class));
    }

    @Test
    public void shouldGetUserInfo() {
        ResponseEntity<User> response = controller.getUserInfo(auth);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(auth, times(1)).getPrincipal();
    }
}
