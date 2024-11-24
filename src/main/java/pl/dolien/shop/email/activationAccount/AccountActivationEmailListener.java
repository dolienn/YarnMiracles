package pl.dolien.shop.email.activationAccount;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountActivationEmailListener {

    private final AccountActivationEmailService accountActivationEmailService;

    @KafkaListener(topics = "send-activation-email", groupId = "user-registration-group")
    public void sendAccountActivationEmail(AccountActivationMessageDTO activationMessageDTO) throws MessagingException {
        accountActivationEmailService.sendActivationEmail(activationMessageDTO);
    }
}
