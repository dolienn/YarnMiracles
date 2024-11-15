package pl.dolien.shop.email.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportEmailSenderTest {

    @InjectMocks
    private SupportEmailSender supportEmailSender;

    @Mock
    private JavaMailSender mailSender;

    private SupportMessageDTO testSupportMessageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldSendToSupportEmail() {
        SimpleMailMessage result = supportEmailSender.sendToSupportEmail(testSupportMessageDTO);

        assertNotNull(result);
        assertEquals(testSupportMessageDTO.getFrom(), result.getFrom());
        assertEquals(testSupportMessageDTO.getSubject(), result.getSubject());
        assertEquals(testSupportMessageDTO.getText(), result.getText());

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    private void initializeTestData() {
        testSupportMessageDTO = SupportMessageDTO.builder()
                .from("test@example.com")
                .subject("Test subject")
                .text("Test text")
                .build();
    }
}