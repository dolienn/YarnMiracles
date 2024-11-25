package pl.dolien.shop.auth.login;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.auth.login.dto.LoginRequestDTO;
import pl.dolien.shop.auth.login.dto.LoginResponseDTO;
import pl.dolien.shop.security.JwtService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_JWT_TOKEN = "test-jwt-token";

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    private LoginRequestDTO testLoginRequestDTO;
    private UserWithRoleDTO testUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldLogin() {
        mockDependencies();

        LoginResponseDTO response = loginService.login(testLoginRequestDTO);

        assertEquals(TEST_JWT_TOKEN, response.getToken());

        verifyInteractions();
    }

    private void initializeTestData() {
        testLoginRequestDTO = LoginRequestDTO.builder()
                .email(TEST_EMAIL)
                .password("password123")
                .build();

        testUserDTO = UserWithRoleDTO.builder()
                .id(1)
                .email(TEST_EMAIL)
                .build();
    }

    private void mockDependencies() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.getUserByAuth(authentication)).thenReturn(testUserDTO);
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn(TEST_JWT_TOKEN);
    }

    private void verifyInteractions() {
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).getUserByAuth(authentication);
        verify(jwtService, times(1)).generateToken(anyMap(), any(User.class));
    }
}