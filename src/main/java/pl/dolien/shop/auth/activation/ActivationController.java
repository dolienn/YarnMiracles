package pl.dolien.shop.auth.activation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class ActivationController {

    private final ActivationService service;

    @GetMapping("/activate")
    public void activateUser(
            @RequestParam String token
    ) {
        service.activateUser(token);
    }
}
