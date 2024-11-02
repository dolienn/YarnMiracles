package pl.dolien.shop.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.common.PageResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FeedbackControllerTest {

    @InjectMocks
    private FeedbackController controller;

    @Mock
    private FeedbackService service;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSaveFeedback() {
        Integer response = controller.saveFeedback(
                FeedbackRequest.builder()
                        .note(5D)
                        .comment("comment")
                        .productId(1L)
                        .build(),
                authentication);

        verify(service, times(1)).save(any(FeedbackRequest.class), any());
    }

    @Test
    public void shouldFindAllFeedbacksByProduct() {
        PageResponse<FeedbackResponse> response = controller.findAllFeedbacksByProduct(1L, 0, 5, authentication);

        verify(service, times(1)).findAllFeedbacksByProduct(1L, 0, 5, authentication);
    }
}
