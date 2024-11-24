package pl.dolien.shop.auth.activation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.token.Token;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ActivationServiceTest {

    @InjectMocks
    private ActivationService activationService;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private KafkaJsonProducer kafkaJsonProducer;

    private Token testToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldActivateUserByToken() {
        when(tokenService.getValidatedToken(anyString())).thenReturn(testToken);

        activationService.activateUser(anyString());

        assertTrue(testToken.getUser().isEnabled());

        verify(tokenService, times(1)).getValidatedToken(anyString());
    }

    private void initializeTestData() {
        User testUser = User.builder()
                .id(1)
                .enabled(false)
                .build();

        testToken = Token.builder()
                .user(testUser)
                .build();
    }
}