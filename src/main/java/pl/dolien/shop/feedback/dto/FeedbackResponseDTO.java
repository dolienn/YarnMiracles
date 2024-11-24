package pl.dolien.shop.feedback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.dto.UserDTO;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FeedbackResponseDTO implements Serializable {

    private Double note;
    private String comment;
    private LocalDateTime createdDate;
    private UserDTO createdBy;
    private boolean ownFeedback;
}
