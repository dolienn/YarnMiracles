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

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class UserInfoController {

    private final UserInfoUpdater service;

    @PostMapping("/update-user-info")
    public void updateUserInfo(
            @RequestBody @Valid RegistrationDTO request,
            Authentication auth
    ) {
        service.updateUserInformation(request, auth);
    }
}
