package pl.dolien.shop.feedback;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.common.PageResponse;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.user.User;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final ProductRepository productRepository;

    private final FeedbackMapper feedbackMapper;

    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("No product found with the ID: " + request.productId()));

        Feedback feedback = feedbackMapper.toFeedback(request);
        User user = ((User) connectedUser.getPrincipal());
        feedback.setCreatedBy(user.getId());
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByProduct(Long productId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        Integer userId;

        if (connectedUser != null) {
            User user = ((User) connectedUser.getPrincipal());
            if (user != null) {
                userId = user.getId();
            } else {
                userId = null;
            }
        } else {
            userId = null;
        }

        Page<Feedback> feedbacks = feedbackRepository.findAllByProductId(productId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, userId != null ? userId : 0))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}