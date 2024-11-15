package pl.dolien.shop.email.activationAccount;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.mockito.Mockito.*;

class AccountActivationEmailServiceTest {

    @InjectMocks
    private AccountActivationEmailService accountActivationEmailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    private AccountActivationMessageDTO accountActivationMessageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setSupportEmail();
        initializeTestData();
    }

    @Test
    void shouldSendAccountActivationEmail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn("Test content");

        accountActivationEmailService.sendActivationEmail(accountActivationMessageDTO);

        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any(Context.class));
    }

    private void setSupportEmail() {
        ReflectionTestUtils.setField(accountActivationEmailService, "supportEmail", "support@example.com");
    }

    private void initializeTestData() {
        accountActivationMessageDTO = AccountActivationMessageDTO.builder()
                .to("test@example.com")
                .subject("Test subject")
                .username("testUsername")
                .activationCode("123456")
                .confirmationUrl("testConfirmationUrl")
                .build();
    }
}