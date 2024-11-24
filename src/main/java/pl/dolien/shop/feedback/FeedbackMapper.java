package pl.dolien.shop.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.dolien.shop.user.UserMapper.toUserDTO;

@Component
@RequiredArgsConstructor
public class FeedbackMapper {

    private final UserService userService;

    public static Feedback toFeedbackWithCreator(FeedbackRequestDTO request, UserWithRoleDTO userDTO) {
        Feedback feedback = toFeedback(request);
        feedback.setCreatedBy(userDTO.getId());
        return feedback;
    }

    public static Feedback toFeedback(FeedbackRequestDTO request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .productId(request.productId())
                .build();
    }

    public static List<FeedbackDTO> toFeedbackDTOs(List<Feedback> feedbacks) {
        return feedbacks.stream()
                .map(FeedbackMapper::toFeedbackDTO)
                .collect(Collectors.toList());
    }

    public static FeedbackDTO toFeedbackDTO(Feedback feedback) {
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .lastModifiedDate(feedback.getLastModifiedDate())
                .createdBy(feedback.getCreatedBy())
                .lastModifiedBy(feedback.getLastModifiedBy())
                .productId(feedback.getProductId())
                .build();
    }

    public Page<FeedbackResponseDTO> toFeedbackResponses(Page<Feedback> feedbacks, Integer userId) {
        List<FeedbackResponseDTO> feedbackResponseDTOs = feedbacks.stream()
                .map(feedback -> toFeedbackResponse(feedback, Objects.equals(feedback.getCreatedBy(), userId)))
                .toList();

        Pageable pageable = feedbacks.getPageable();

        return new RestPage<>(feedbackResponseDTOs, pageable.getPageNumber(), pageable.getPageSize(), feedbacks.getTotalElements());
    }

    public FeedbackResponseDTO toFeedbackResponse(Feedback feedback, boolean isOwnFeedback) {
        return FeedbackResponseDTO.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .createdBy(toUserDTO(userService.getUserById(feedback.getCreatedBy())))
                .ownFeedback(isOwnFeedback)
                .build();
    }
}
