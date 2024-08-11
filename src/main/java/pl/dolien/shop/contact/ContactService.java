package pl.dolien.shop.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final JavaMailSender mailSender;

    public void sendMessage(ContactRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("thedolien@gmail.com");
        message.setSubject(request.getSubject());
        message.setText(request.getMessage());
        message.setFrom(request.getEmail());

        mailSender.send(message);
    }
}
