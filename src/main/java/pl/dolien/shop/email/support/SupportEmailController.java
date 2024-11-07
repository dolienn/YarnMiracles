package pl.dolien.shop.email.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("support")
@RequiredArgsConstructor
@Tag(name = "SupportEmail")
public class SupportEmailController {

    private final SupportEmailService service;

    @PostMapping("send-message")
    public void sendMessage(
            @RequestBody @Valid SupportMessageDTO request
    ) {
        service.sendSupportEmail(request);
    }
}
