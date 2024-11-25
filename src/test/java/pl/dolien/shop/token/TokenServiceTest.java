package pl.dolien.shop.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.exception.ExpiredTokenException;
import pl.dolien.shop.exception.InvalidTokenException;
import pl.dolien.shop.exception.TokenAlreadyUsedException;
import pl.dolien.shop.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private static final String TOKEN_EXPIRED_MESSAGE = "Activation token has expired";
    private static final String TOKEN_USED_MESSAGE = "Activation token has already been used";
    private static final String TOKEN_INVALID_MESSAGE = "Invalid token";

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenRepository tokenRepository;

    private User testUser;
    private Token testToken;
    private Token testExpiredToken;
    private Token testUsedToken;

    private static final int DEFAULT_CODE_LENGTH = 6;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initializeTestData();
    }

    @Test
    void shouldGenerateActivationToken() {
        when(tokenRepository.save(testToken)).thenReturn(any(Token.class));

        String generatedToken = tokenService.generateActivationToken(testUser);
        String generatedToken2 = tokenService.generateActivationToken(testUser);

        assertGeneratedToken(generatedToken, generatedToken2);
        verify(tokenRepository, times(2)).save(any(Token.class));
    }

    @Test
    void shouldSaveToken() {
        when(tokenRepository.save(testToken)).thenReturn(testToken);

        Token savedToken = tokenService.saveToken(testToken);

        assertSavedToken(savedToken);
        verify(tokenRepository, times(1)).save(testToken);
    }

    @Test
    void shouldReturnValidatedToken() {
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.of(testToken));

        Token validatedToken = tokenService.getValidatedToken(testToken.getToken());

        assertValidatedToken(validatedToken);
        verify(tokenRepository, times(1)).findByToken(testToken.getToken());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {
        when(tokenRepository.findByToken(testExpiredToken.getToken())).thenReturn(Optional.of(testExpiredToken));

        ExpiredTokenException exception = assertThrows(ExpiredTokenException.class,
                () -> tokenService.getValidatedToken(testExpiredToken.getToken()));

        assertEquals(TOKEN_EXPIRED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsAlreadyUsed() {
        when(tokenRepository.findByToken(testUsedToken.getToken())).thenReturn(Optional.of(testUsedToken));

        TokenAlreadyUsedException exception = assertThrows(TokenAlreadyUsedException.class,
                () -> tokenService.getValidatedToken(testUsedToken.getToken()));

        assertEquals(TOKEN_USED_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        when(tokenRepository.findByToken(testToken.getToken())).thenReturn(Optional.empty());

        InvalidTokenException exception = assertThrows(InvalidTokenException.class,
                () -> tokenService.getValidatedToken(testToken.getToken()));

        assertEquals(TOKEN_INVALID_MESSAGE, exception.getMessage());
    }

    private void initializeTestData() {
        testUser = User.builder()
                .id(1)
                .email("test@example.com")
                .build();

        testToken = Token.builder()
                .id(1)
                .token("testToken123")
                .user(testUser)
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .build();

        testExpiredToken = Token.builder()
                .id(2)
                .token("testExpiredToken123")
                .user(testUser)
                .expiresAt(LocalDateTime.now().minusMinutes(25))
                .build();

        testUsedToken = Token.builder()
                .id(3)
                .token("testUsedToken123")
                .user(testUser)
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .used(true)
                .build();
    }

    private void assertGeneratedToken(String generatedToken, String generatedToken2) {
        assertNotNull(generatedToken);
        assertEquals(DEFAULT_CODE_LENGTH, generatedToken.length());
        assertNotEquals(generatedToken, generatedToken2);
    }

    private void assertSavedToken(Token savedToken) {
        assertNotNull(savedToken);
        assertEquals(testToken.getId(), savedToken.getId());
        assertEquals(testToken.getToken(), savedToken.getToken());
    }

    private void assertValidatedToken(Token validatedToken) {
        assertNotNull(validatedToken);
        assertEquals(testToken.getId(), validatedToken.getId());
        assertEquals(testToken.getToken(), validatedToken.getToken());
    }
}
