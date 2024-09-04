package pl.dolien.shop.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dolien.shop.product.Product;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackMapperTest {

    private FeedbackMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new FeedbackMapper();
    }

    @Test
    public void shouldMapFeedbackRequestToFeedback() {
        FeedbackRequest request = new FeedbackRequest(
                5D,
                "What an amazing product!",
                2L
        );

        Feedback feedback = mapper.toFeedback(request);

        assertEquals(request.note(), feedback.getNote());
        assertEquals(request.comment(), feedback.getComment());
        assertNotNull(feedback.getProduct());
        assertEquals(request.productId(), feedback.getProduct().getId());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenFeedbackRequestIsNull() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.toFeedback(null));
        assertEquals("The feedback request should not be null", exp.getMessage());

    }

    @Test
    public void shouldMapFeedbackToFeedbackResponse() {
        int userId = 2;
        Feedback feedback = new Feedback(
                1,
                3D,
                "Nah. It's not bad but.. you know, should be better",
                Product.builder().id(4L).build(),
                LocalDateTime.now(),
                null,
                userId,
                1
        );

        FeedbackResponse response = mapper.toFeedbackResponse(feedback, userId);

        assertEquals(feedback.getNote(), response.getNote());
        assertEquals(feedback.getComment(), response.getComment());
        assertEquals(feedback.getCreatedDate(), response.getCreatedDate());
        assertEquals(feedback.getCreatedBy(), response.getCreatedBy());
        assertTrue(response.isOwnFeedback());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenFeedbackIsNull() {
        var exp = assertThrows(NullPointerException.class, () -> mapper.toFeedbackResponse(null, 2));
        assertEquals("The feedback should not be null", exp.getMessage());

    }
}