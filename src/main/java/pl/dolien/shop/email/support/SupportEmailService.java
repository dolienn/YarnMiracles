package pl.dolien.shop.email.support;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportEmailService {

    private final JavaMailSender mailSender;

    public void sendSupportEmail(SupportMessageDTO request) {
        SimpleMailMessage message = buildSupportEmailMessage(request);
        mailSender.send(message);
    }

    private SimpleMailMessage buildSupportEmailMessage(SupportMessageDTO request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("thedolien@gmail.com");
        message.setFrom(request.getFrom());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        return message;
    }
}
