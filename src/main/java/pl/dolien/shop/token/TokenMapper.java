package pl.dolien.shop.token;

import pl.dolien.shop.user.User;

import java.time.LocalDateTime;

public class TokenMapper {

    public static Token toToken(String generatedToken, User user) {
        return Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(25))
                .user(user)
                .build();
    }
}
