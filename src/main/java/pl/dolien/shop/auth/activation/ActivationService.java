package pl.dolien.shop.auth.activation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.summaryMetrics.SummaryMetricsService;
import pl.dolien.shop.token.Token;
import pl.dolien.shop.token.TokenService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final TokenService tokenService;
    private final UserService userService;
    private final SummaryMetricsService summaryMetricsService;

    public void activateUser(String token) {
        Token savedToken = tokenService.getValidatedToken(token);
        enableUser(savedToken.getUser());
        markTokenAsValidated(savedToken);
    }

    private void enableUser(User user) {
        summaryMetricsService.incrementUserCount();

        user.setEnabled(true);
        userService.saveUser(user);
    }

    private void markTokenAsValidated(Token token) {
        token.setValidatedAt(LocalDateTime.now());
        token.setUsed(true);
        tokenService.saveToken(token);
    }
}
