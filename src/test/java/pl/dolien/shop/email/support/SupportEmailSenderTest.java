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

        assertEmailContent(result, testSupportMessageDTO);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    private void initializeTestData() {
        testSupportMessageDTO = SupportMessageDTO.builder()
                .from("test@example.com")
                .subject("Test subject")
                .text("Test text")
                .build();
    }

    private void assertEmailContent(SimpleMailMessage result, SupportMessageDTO dto) {
        assertNotNull(result);
        assertEquals(dto.getFrom(), result.getFrom());
        assertEquals(dto.getSubject(), result.getSubject());
        assertEquals(dto.getText(), result.getText());
    }
}