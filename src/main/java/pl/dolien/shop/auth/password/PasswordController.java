package pl.dolien.shop.auth.password;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class PasswordController {

    private final PasswordChanger service;

    @PostMapping("/change-password")
    public void changePassword(
            @RequestBody @Valid ChangePasswordDTO request,
            Authentication connectedUser
    ) {
        service.changePassword(request, connectedUser);
    }
}
