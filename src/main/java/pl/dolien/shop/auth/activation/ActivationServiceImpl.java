package pl.dolien.shop.auth.activation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.token.Token;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationServiceImpl implements ActivationService {

    private final TokenService tokenService;
    private final KafkaJsonProducer kafkaJsonProducer;

    @Transactional
    @Override
    public void activateUser(String token) {
        Token savedToken = tokenService.getValidatedToken(token);

        kafkaJsonProducer.sendMessageToIncrementUserCount(1);

        enableUser(savedToken.getUser());
        markTokenAsValidated(savedToken);
    }

    private void enableUser(User user) {
        user.setEnabled(true);
    }

    private void markTokenAsValidated(Token token) {
        token.setValidatedAt(LocalDateTime.now());
        token.setUsed(true);
    }
}
