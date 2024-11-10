package pl.dolien.shop.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.dashboard.DashboardService;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationParams;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.List;

import static pl.dolien.shop.feedback.FeedbackMapper.toFeedbackResponses;
import static pl.dolien.shop.feedback.FeedbackMapper.toFeedbackWithCreator;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PageableBuilder pageableBuilder;
    private final DashboardService dashboardService;
    private final UserService userService;

    @CacheEvict(cacheNames = "feedbacksByProduct", allEntries = true)
    public Feedback saveFeedback(FeedbackRequestDTO request, Authentication connectedUser) {
        User user = userService.getUserByAuth(connectedUser);
        Feedback feedback = toFeedbackWithCreator(request, user);
        dashboardService.incrementCustomerFeedbackCount();

        return feedbackRepository.save(feedback);
    }

    @Cacheable(cacheNames = "feedbacksByProduct", keyGenerator = "customKeyGenerator")
    public List<FeedbackResponseDTO> getFeedbacksByProduct(Long productId,
                                                           PaginationParams paginationParams,
                                                           Authentication connectedUser) {
        Pageable pageable = pageableBuilder.buildPageable(paginationParams);
        Integer userId = ((User) connectedUser.getPrincipal()).getId();
        List<Feedback> feedbacks = feedbackRepository.findAllByProductId(productId, pageable);

        return toFeedbackResponses(feedbacks, userId);
    }
}
