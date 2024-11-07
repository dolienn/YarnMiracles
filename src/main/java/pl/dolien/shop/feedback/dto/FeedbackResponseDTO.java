package pl.dolien.shop.feedback.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {

    private Double note;
    private String comment;
    private LocalDateTime createdDate;
    private Integer createdBy;
    private boolean ownFeedback;
}
