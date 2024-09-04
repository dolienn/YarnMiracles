package pl.dolien.shop.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendEmailWhenRequestIsValid() {
        ContactRequest request = ContactRequest.builder()
                .email("test@example.com")
                .subject("Test Subject")
                .message("Test Message")
                .build();

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo("thedolien@gmail.com");
        expectedMessage.setSubject(request.getSubject());
        expectedMessage.setText(request.getMessage());
        expectedMessage.setFrom(request.getEmail());

        contactService.sendMessage(request);

        verify(mailSender, times(1)).send(expectedMessage);
    }

    @Test
    public void sendMessage_ShouldThrowNullPointerException_WhenRequestIsNull() {
        var exp = assertThrows(NullPointerException.class, () -> contactService.sendMessage(null));

        assert exp.getMessage().equals("Contact request not found");
    }
}
