package pl.dolien.shop.auth.registration;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping("/register")
    public void register(
            @RequestBody @Valid RegistrationDTO request
    ) throws MessagingException, RoleNotFoundException {
        service.registerUser(request);
    }
}
