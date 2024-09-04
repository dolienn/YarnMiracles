package pl.dolien.shop.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendEmail_ShouldSendEmailWithCorrectTemplate() throws MessagingException {
        String to = "test@example.com";
        String username = "user";
        EmailTemplateName templateName = EmailTemplateName.ACTIVATE_ACCOUNT;
        String confirmationUrl = "http://example.com";
        String activationCode = "123456";
        String subject = "Test Subject";
        String emailContent = "<html><body>Test Email</body></html>";
        when(templateEngine.process(eq("ACTIVATE_ACCOUNT"), any(Context.class))).thenReturn(emailContent);


        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(to, username, templateName, confirmationUrl, activationCode, subject);

        verify(templateEngine).process(eq("ACTIVATE_ACCOUNT"), any(Context.class));
        verify(mailSender).send(mimeMessage);
    }
}
