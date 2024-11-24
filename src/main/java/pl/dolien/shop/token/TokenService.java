package pl.dolien.shop.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ExpiredTokenException;
import pl.dolien.shop.exception.InvalidTokenException;
import pl.dolien.shop.exception.TokenAlreadyUsedException;
import pl.dolien.shop.user.User;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static pl.dolien.shop.token.TokenMapper.toToken;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String CHARACTERS = "0123456789";
    private static final int DEFAULT_CODE_LENGTH = 6;
    private final SecureRandom secureRandom = new SecureRandom();

    private final TokenRepository tokenRepository;

    public String generateActivationToken(User user) {
        String generatedToken = generateActivationCode();

        Token token = toToken(generatedToken, user);
        saveToken(token);
        return token.getToken();
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    public Token getValidatedToken(String token) {
        Token savedToken = getToken(token);

        if (isTokenExpired(savedToken.getExpiresAt()))
            throw new ExpiredTokenException("Activation token has expired");

        if (savedToken.isUsed())
            throw new TokenAlreadyUsedException("Activation token has already been used");

        return savedToken;
    }

    private String generateActivationCode() {
        return secureRandom.ints(DEFAULT_CODE_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private Token getToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
    }

    private static boolean isTokenExpired(LocalDateTime expirationTime) {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}

