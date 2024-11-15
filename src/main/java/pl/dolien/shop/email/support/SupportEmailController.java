package pl.dolien.shop.email.support;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("support")
@RequiredArgsConstructor
@Tag(name = "SupportEmail")
public class SupportEmailController {

    private final SupportEmailSender sender;

    @PostMapping("send-message")
    public SimpleMailMessage sendMessage(
            @RequestBody @Valid SupportMessageDTO request
    ) {
        return sender.sendToSupportEmail(request);
    }
}
