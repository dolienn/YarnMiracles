package pl.dolien.shop.feedback;

import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackMapper {

    public static Feedback toFeedbackWithCreator(FeedbackRequestDTO request, User user) {
        Feedback feedback = toFeedback(request);
        feedback.setCreatedBy(user.getId());
        return feedback;
    }

    public static Feedback toFeedback(FeedbackRequestDTO request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .productId(request.productId())
                .build();
    }

    public static List<FeedbackResponseDTO> toFeedbackResponses(List<Feedback> feedbacks, Integer userId) {
        return feedbacks.stream()
                .map(feedback -> toFeedbackResponse(feedback, userId != null))
                .toList();
    }

    public static FeedbackResponseDTO toFeedbackResponse(Feedback feedback, boolean isOwnFeedback) {
        return FeedbackResponseDTO.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .createdBy(feedback.getCreatedBy())
                .ownFeedback(isOwnFeedback)
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
}
