package pl.dolien.shop.auth.password;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.user.dto.UserDTO;

import static pl.dolien.shop.user.UserMapper.toUserDTO;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class PasswordController {

    private final PasswordChanger service;

    @PostMapping("/change-password")
    public UserDTO changePassword(
            @RequestBody @Valid PasswordRequestDTO request,
            Authentication connectedUser
    ) {
        return toUserDTO(service.changePassword(request, connectedUser));
    }
}
