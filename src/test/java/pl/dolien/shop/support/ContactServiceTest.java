package pl.dolien.shop.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.dolien.shop.email.support.SupportMessageDTO;
import pl.dolien.shop.email.support.SupportEmailService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContactServiceTest {

    @InjectMocks
    private SupportEmailService contactService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendEmailWhenRequestIsValid() {
        SupportMessageDTO request = SupportMessageDTO.builder()
                .from("test@example.com")
                .subject("Test Subject")
                .text("Test Message")
                .build();

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo("thedolien@gmail.com");
        expectedMessage.setSubject(request.getSubject());
        expectedMessage.setText(request.getText());
        expectedMessage.setFrom(request.getFrom());

        contactService.sendSupportEmail(request);

        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    public void sendMessage_ShouldThrowNullPointerException_WhenRequestIsNull() {
        var exp = assertThrows(NullPointerException.class, () -> contactService.sendSupportEmail(null));

        assert exp.getMessage().equals("Contact request not found");
    }
}
