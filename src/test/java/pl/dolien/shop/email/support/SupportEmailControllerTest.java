package pl.dolien.shop.email.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SupportEmailControllerTest {

    @InjectMocks
    private SupportEmailController supportEmailController;

    @Mock
    private SupportEmailSender supportEmailSender;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SupportMessageDTO testSupportMessageDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(supportEmailController).build();

        initializeTestData();
    }

    @Test
    void shouldSendMessageToSupportEmail() throws Exception {
        when(supportEmailSender.sendToSupportEmail(testSupportMessageDTO)).thenReturn(any(SimpleMailMessage.class));

        mockMvc.perform(post("/support/send-message")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testSupportMessageDTO)))
                .andExpect(status().isOk());

        verify(supportEmailSender, times(1)).sendToSupportEmail(any(SupportMessageDTO.class));
    }

    private void initializeTestData() {
        testSupportMessageDTO = SupportMessageDTO.builder()
                .from("test@example.com")
                .subject("Test subject")
                .text("Test text")
                .build();
    }
}