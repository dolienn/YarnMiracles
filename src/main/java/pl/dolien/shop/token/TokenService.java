package pl.dolien.shop.token;

import pl.dolien.shop.user.User;

public interface TokenService {

    String generateActivationToken(User user);

    Token saveToken(Token token);

    Token getValidatedToken(String token);
}

