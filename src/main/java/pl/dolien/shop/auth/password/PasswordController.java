package pl.dolien.shop.auth.password;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.user.dto.UserDTO;

import static pl.dolien.shop.user.UserMapper.toUserDTO;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class PasswordController {

    private final PasswordChanger passwordChanger;

    @PostMapping("/change-password")
    public UserDTO changePassword(
            @RequestBody @Valid PasswordRequestDTO request,
            Authentication connectedUser
    ) {
        return toUserDTO(passwordChanger.changePassword(request, connectedUser));
    }
}
