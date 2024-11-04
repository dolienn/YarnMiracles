package pl.dolien.shop.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.dashboard.DashboardDataRepository;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    private final FeedbackRepository feedbackRepository;

    private final PageableBuilder pageableBuilder;

    private final DashboardDataRepository dashboardDataRepository;

    private final UserService userService;

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

    public Page<FeedbackResponse> getAllFeedbacksByProduct(Long productId,
                                                           PageRequestParams pageRequestParams,
                                                           Authentication connectedUser) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        Integer userId = userService.getUserByAuth(connectedUser).getId();

        Page<Feedback> feedbacks = feedbackRepository.findAllByProductId(productId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, userId != null ? userId : 0))
                .toList();
        return new PageImpl<>(feedbackResponses, pageable, feedbacks.getTotalElements());
    }
}
