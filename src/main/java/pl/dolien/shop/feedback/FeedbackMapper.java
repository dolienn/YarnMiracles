package pl.dolien.shop.feedback;

import org.springframework.stereotype.Service;
import pl.dolien.shop.product.Product;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest request) {
        if(request == null) {
            throw new NullPointerException("The feedback request should not be null");
        }
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .product(Product.builder()
                        .id(request.productId())
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        if(feedback == null) {
            throw new NullPointerException("The feedback should not be null");
        }
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .createdBy(feedback.getCreatedBy())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
