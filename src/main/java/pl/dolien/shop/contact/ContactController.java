package pl.dolien.shop.contact;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.auth.PasswordRequest;

@RestController
@RequestMapping("contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> sendMessage(
            @RequestBody @Valid ContactRequest request
    ) {
        service.sendMessage(request);
        return ResponseEntity.accepted().build();
    }
}
