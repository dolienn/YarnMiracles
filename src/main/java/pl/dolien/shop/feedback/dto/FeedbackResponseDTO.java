package pl.dolien.shop.feedback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class FeedbackResponseDTO {

    private Double note;
    private String comment;
    private LocalDateTime createdDate;
    private Integer createdBy;
    private boolean ownFeedback;
}
