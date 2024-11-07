package pl.dolien.shop.auth.userInfo;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolien.shop.auth.registration.RegistrationDTO;
import pl.dolien.shop.user.dto.UserDTO;

import static pl.dolien.shop.user.UserMapper.toUserDTO;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class UserInfoController {

    private final UserInfoUpdater service;

    @PostMapping("/update-user-info")
    public UserDTO updateUserInfo(
            @RequestBody @Valid RegistrationDTO request,
            Authentication auth
    ) {
        return toUserDTO(service.updateUserInformation(request, auth));
    }
}
