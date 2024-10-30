package pl.dolien.shop.auth;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dolien.shop.email.EmailService;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleRepository;
import pl.dolien.shop.security.JwtService;
import pl.dolien.shop.token.Token;
import pl.dolien.shop.token.TokenRepository;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldRegister() throws MessagingException {
        var request = new RegistrationRequest("John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "password123");
        var role = new Role(1, "USER", List.of(), LocalDateTime.now(), LocalDateTime.now());

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        authenticationService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenRoleUserNotFound() {
        var request = new RegistrationRequest("John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "password123");

        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        var exp = assertThrows(IllegalStateException.class, () -> authenticationService.register(request));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("ROLE USER was not initialized", exp.getMessage());
    }

    @Test
    public void shouldChangeAccountDetails() {
        var request = new RegistrationRequest("John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "password123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(user);

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        authenticationService.changeAccountDetails(request, auth);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        var request = new RegistrationRequest("John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "password123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(user);

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        when(userRepository.save(user)).thenReturn(user);

        var exp = assertThrows(IllegalArgumentException.class, () -> authenticationService.changeAccountDetails(request, auth));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("Passwords do not match.", exp.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenThereIsAUserWithTheSameEmail() {
        var request = new RegistrationRequest("John", "Doe", "john.doe@example.com", LocalDate.of(1990, 1, 1), "password123");
        Authentication auth = mock(Authentication.class);

        User currentUser = User.builder()
                .id(1)
                .email("current.user@example.com")
                .password("password123")
                .build();

        User existingUser = User.builder()
                .id(2)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(currentUser);

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(existingUser));

        var exp = assertThrows(IllegalArgumentException.class, () -> authenticationService.changeAccountDetails(request, auth));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("There is a user on the same email.", exp.getMessage());
    }

    @Test
    public void shouldChangePassword() {
        var request = new PasswordRequest("password123", "newPassword123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        authenticationService.changePassword(request, auth);

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        var request = new PasswordRequest("password123", "newPassword123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        var exp = assertThrows(IllegalArgumentException.class, () -> authenticationService.changePassword(request, auth));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("The current password does not match the one entered in the form.", exp.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenNewPasswordIsTheSame() {
        var request = new PasswordRequest("password123", "password123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(auth.getPrincipal()).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));

        var exp = assertThrows(IllegalArgumentException.class, () -> authenticationService.changePassword(request, auth));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("The current password and the new one are the same.", exp.getMessage());
    }

    @Test
    public void shouldAuthenticateUser() {
        var request = new AuthenticationRequest("john.doe@example.com", "password123");
        Authentication auth = mock(Authentication.class);

        User user = User.builder()
                .id(1)
                .email("john.doe@example.com")
                .password("password123")
                .build();

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(user);

        var claims = new HashMap<String, Object>();
        claims.put("fullName", "John Doe");
        when(jwtService.generateToken(claims, user)).thenReturn("token123");
        authenticationService.authenticate(request);
    }

    @Test
    public void shouldActivateAccount() throws MessagingException {
        var token = Token.builder()
                .id(1)
                .token("token123")
                .user(User.builder()
                        .id(1)
                        .email("john.doe@example.com")
                        .build())
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .build();

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(token.getUser()));
        when(userRepository.save(any(User.class))).thenReturn(token.getUser());
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        authenticationService.activateAccount(token.getToken());

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    public void shouldThrowExceptionWhenTokenIsInvalid() {
        var token = Token.builder()
                .id(1)
                .token("token123")
                .user(User.builder()
                        .id(1)
                        .email("john.doe@example.com")
                        .build())
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .build();

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());

        var exp = assertThrows(RuntimeException.class, () -> authenticationService.activateAccount(token.getToken()));

        verify(userRepository, never()).save(any(User.class));
        verify(tokenRepository, never()).save(any(Token.class));
        assertEquals("Invalid token", exp.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenTokenIsExpired() {
        var token = Token.builder()
                .id(1)
                .token("token123")
                .user(User.builder()
                        .id(1)
                        .email("john.doe@example.com")
                        .build())
                .expiresAt(LocalDateTime.now().minusMinutes(25))
                .build();

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));

        var exp = assertThrows(RuntimeException.class, () -> authenticationService.activateAccount(token.getToken()));

        verify(userRepository, never()).save(any(User.class));
        assertEquals("Activation token has expired. " +
                "A new token has been sent to the same email address", exp.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenUserDoesNotExist() {
        var token = Token.builder()
                .id(1)
                .token("token123")
                .user(User.builder()
                        .id(1)
                        .email("john.doe@example.com")
                        .build())
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .build();

        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        var exp = assertThrows(UsernameNotFoundException.class, () -> authenticationService.activateAccount(token.getToken()));

        verify(userRepository, never()).save(any(User.class));
        verify(tokenRepository, never()).save(any(Token.class));
        assertEquals("User not found", exp.getMessage());
    }
}
