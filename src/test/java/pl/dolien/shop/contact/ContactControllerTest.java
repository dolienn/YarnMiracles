package pl.dolien.shop.contact;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ContactControllerTest {

    @InjectMocks
    private ContactController controller;

    @Mock
    private ContactService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendMessage() {
        ResponseEntity<?> response = controller.sendMessage(ContactRequest.builder().build());

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(service, times(1)).sendMessage(any(ContactRequest.class));
    }
}
