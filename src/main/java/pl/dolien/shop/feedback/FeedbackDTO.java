package pl.dolien.shop.feedback;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.dolien.shop.pagination.PageRequestParams;

@Getter
@Setter
@Builder
public class FeedbackDTO {
    private Long productId;
    private PageRequestParams pageRequestParams;
}
