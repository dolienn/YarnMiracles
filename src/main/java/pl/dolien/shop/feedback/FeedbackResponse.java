package pl.dolien.shop.feedback;

import lombok.*;
import pl.dolien.shop.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private Double note;
    private String comment;
    private LocalDateTime createdDate;
    private Integer createdBy;
    private boolean ownFeedback;
}
