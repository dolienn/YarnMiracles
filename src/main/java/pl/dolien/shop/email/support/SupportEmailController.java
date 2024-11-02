package pl.dolien.shop.email.support;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("support")
@RequiredArgsConstructor
public class SupportEmailController {

    private final SupportEmailService service;

    @PostMapping("send-message")
    public void sendMessage(
            @RequestBody @Valid SupportMessageDTO request
    ) {
        service.sendSupportEmail(request);
    }
}
