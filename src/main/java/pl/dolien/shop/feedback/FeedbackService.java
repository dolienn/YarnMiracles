package pl.dolien.shop.feedback;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PaginationParams;

public interface FeedbackService {

    FeedbackDTO saveFeedback(FeedbackRequestDTO request, Authentication connectedUser);

    Page<FeedbackResponseDTO> getFeedbacksByProduct(Long productId, PaginationParams paginationParams, Authentication connectedUser);
}

