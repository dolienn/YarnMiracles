package pl.dolien.shop.auth.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.exception.UserAlreadyExistsException;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleService;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    private static final String USER_EMAIL = "test@example.com";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String USER_ALREADY_EXISTS_MESSAGE = "User with email " + USER_EMAIL + " already exists";

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserService userService;

    @Mock
    private KafkaJsonProducer kafkaJsonProducer;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegistrationDTO testRegistrationDTO;
    private Role userRole;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldRegisterUser() throws RoleNotFoundException {
        mockSuccessfulRegistration();

        User response = registrationService.registerUser(testRegistrationDTO);

        assertRegistrationResponse(response);

        verifySuccessfulRegistrationInteractions();
    }

    @Test
    void shouldThrowExceptionWhenUserExists() {
        when(userService.isUserExists(testRegistrationDTO.getEmail())).thenReturn(true);

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.registerUser(testRegistrationDTO)
        );

        assertEquals(USER_ALREADY_EXISTS_MESSAGE, exception.getMessage());
    }

    private void initializeTestData() {
        testRegistrationDTO = RegistrationDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email(USER_EMAIL)
                .dateOfBirth(LocalDate.parse("2020-01-01"))
                .password("password123")
                .build();

        userRole = Role.builder()
                .id(1)
                .name("USER")
                .build();

        testUser = User.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email(USER_EMAIL)
                .dateOfBirth(LocalDate.parse("2020-01-01"))
                .password(ENCODED_PASSWORD)
                .build();
    }

    private void mockSuccessfulRegistration() throws RoleNotFoundException {
        when(userService.isUserExists(testRegistrationDTO.getEmail())).thenReturn(false);
        when(roleService.getByName("USER")).thenReturn(userRole);
        when(passwordEncoder.encode(testRegistrationDTO.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(tokenService.generateActivationToken(any(User.class))).thenReturn("token123");
        when(userService.saveUser(any(User.class))).thenReturn(testUser);
    }

    private void assertRegistrationResponse(User response) {
        assertNotNull(response);
        assertEquals(testRegistrationDTO.getFirstname(), response.getFirstname());
        assertEquals(testRegistrationDTO.getLastname(), response.getLastname());
        assertEquals(testRegistrationDTO.getEmail(), response.getEmail());
        assertEquals(testRegistrationDTO.getDateOfBirth(), response.getDateOfBirth());
        assertEquals(ENCODED_PASSWORD, response.getPassword());
    }

    private void verifySuccessfulRegistrationInteractions() throws RoleNotFoundException {
        verify(userService, times(1)).isUserExists(testRegistrationDTO.getEmail());
        verify(roleService, times(1)).getByName("USER");
        verify(passwordEncoder, times(1)).encode(testRegistrationDTO.getPassword());
        verify(tokenService, times(1)).generateActivationToken(any(User.class));
        verify(userService, times(1)).saveUser(any(User.class));
    }
}