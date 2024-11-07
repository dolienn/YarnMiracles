//package pl.dolien.shop.email;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.thymeleaf.context.Context;
//import org.thymeleaf.spring6.SpringTemplateEngine;
//import pl.dolien.shop.email.activationAccount.AccountActivationEmailService;
//import pl.dolien.shop.email.activationAccount.AccountActivationMessageDTO;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//public class AccountActivationEmailServiceTest {
//
//    @InjectMocks
//    private AccountActivationEmailService accountActivationEmailService;
//
//    @Mock
//    private JavaMailSender mailSender;
//
//    @Mock
//    private SpringTemplateEngine templateEngine;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void sendEmail_ShouldSendEmailWithCorrectTemplate() throws MessagingException {
//        AccountActivationMessageDTO accountActivationMessageDTO = AccountActivationMessageDTO.builder()
//                .emailTemplate(EmailTemplateName.ACTIVATE_ACCOUNT)
//                .username("John Doe")
//                .confirmationUrl("https://example.com/confirm")
//                .activationCode("123456")
//                .build();
//        String emailContent = "<html><body>Test Email</body></html>";
//        when(templateEngine.process(eq("ACTIVATE_ACCOUNT"), any(Context.class))).thenReturn(emailContent);
//
//
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//        accountActivationEmailService.sendActivationEmail(accountActivationMessageDTO);
//
//        verify(templateEngine).process(eq("ACTIVATE_ACCOUNT"), any(Context.class));
//        verify(mailSender).send(mimeMessage);
//    }
//}
