package pl.dolien.shop.auth.login;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolien.shop.auth.login.dto.LoginRequestDTO;
import pl.dolien.shop.auth.login.dto.LoginResponseDTO;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class LoginController {

    private final LoginService service;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody @Valid LoginRequestDTO request
    ) {
        return service.login(request);
    }
}
