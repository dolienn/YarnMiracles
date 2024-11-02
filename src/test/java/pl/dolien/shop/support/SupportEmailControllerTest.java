package pl.dolien.shop.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.email.support.SupportEmailController;
import pl.dolien.shop.email.support.SupportMessageDTO;
import pl.dolien.shop.email.support.SupportEmailService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SupportEmailControllerTest {

    @InjectMocks
    private SupportEmailController controller;

    @Mock
    private SupportEmailService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendMessage() {
        controller.sendMessage(SupportMessageDTO.builder().build());

        verify(service, times(1)).sendSupportEmail(any(SupportMessageDTO.class));
    }
}
