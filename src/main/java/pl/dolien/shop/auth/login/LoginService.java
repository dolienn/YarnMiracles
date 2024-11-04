package pl.dolien.shop.auth.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.dolien.shop.security.JwtService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userService.getUserByAuth(auth);

        String jwtToken = jwtService.generateToken(Map.of("fullName", user.getFullName()), user);

        return LoginResponse.builder()
                .token(jwtToken).build();
    }
}
