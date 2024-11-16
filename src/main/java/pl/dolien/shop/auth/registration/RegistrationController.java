package pl.dolien.shop.auth.registration;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.user.dto.UserDTO;

import javax.management.relation.RoleNotFoundException;

import static pl.dolien.shop.user.UserMapper.toUserDTO;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping("/register")
    public UserDTO register(
            @RequestBody @Valid RegistrationDTO dto
    ) throws MessagingException, RoleNotFoundException {
        return toUserDTO(service.registerUser(dto));
    }
}
