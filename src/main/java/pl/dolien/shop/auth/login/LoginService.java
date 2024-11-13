package pl.dolien.shop.auth.login;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.dolien.shop.auth.login.dto.LoginRequestDTO;
import pl.dolien.shop.auth.login.dto.LoginResponseDTO;
import pl.dolien.shop.security.JwtService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.Map;

import static pl.dolien.shop.user.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO login(LoginRequestDTO request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = toUser(userService.getUserDTOByAuth(auth));

        String jwtToken = jwtService.generateToken(Map.of("fullName", user.getFullName()), user);

        return LoginResponseDTO.builder()
                .token(jwtToken).build();
    }
}
