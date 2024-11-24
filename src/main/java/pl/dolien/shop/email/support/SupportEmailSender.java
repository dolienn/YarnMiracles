package pl.dolien.shop.email.support;

import org.springframework.mail.SimpleMailMessage;

public interface SupportEmailSender {

    SimpleMailMessage sendToSupportEmail(SupportMessageDTO request);
}

