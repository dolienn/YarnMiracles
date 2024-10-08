package pl.dolien.shop.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.common.PageResponse;
import pl.dolien.shop.dashboard.DashboardDataRepository;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.user.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    private final FeedbackRepository feedbackRepository;

    private final ProductRepository productRepository;

    private final DashboardDataRepository dashboardDataRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        if(request == null) {
            throw new NullPointerException("Feedback request should not be null");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        User user = ((User) connectedUser.getPrincipal());
        feedback.setCreatedBy(user.getId());
        dashboardDataRepository.findById(1L).ifPresent(dashboardData -> {
            dashboardData.setTotalCustomerFeedback(dashboardData.getTotalCustomerFeedback() + 1);
            dashboardDataRepository.save(dashboardData);
        });
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByProduct(Long productId, int page, int size, Authentication connectedUser) {

        if(productId == 0) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        if(productRepository.findById(productId).isEmpty()) {
            throw new NullPointerException("Can't find a product with the id");
        }

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
