package pl.dolien.shop.product.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import pl.dolien.shop.feedback.dto.FeedbackDTO;

import java.util.List;

@Getter
@SuperBuilder
public class ProductWithFeedbackDTO extends ProductDTO {
    private List<FeedbackDTO> feedbacks;
}
