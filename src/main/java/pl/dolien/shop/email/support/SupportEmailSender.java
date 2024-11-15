package pl.dolien.shop.email.support;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportEmailSender {

    @Value("${support.email}")
    private final String supportEmail;

    private final JavaMailSender mailSender;

    public SimpleMailMessage sendToSupportEmail(SupportMessageDTO request) {
        SimpleMailMessage message = buildSupportEmailMessage(request);
        mailSender.send(message);
        return message;
    }

    private SimpleMailMessage buildSupportEmailMessage(SupportMessageDTO request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(supportEmail);
        message.setFrom(request.getFrom());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        return message;
    }
}
