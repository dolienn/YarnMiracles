package pl.dolien.shop.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationParams;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import static pl.dolien.shop.feedback.FeedbackMapper.*;
import static pl.dolien.shop.user.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final PageableBuilder pageableBuilder;
    private final UserService userService;
    private final FeedbackMapper feedbackMapper;
    private final KafkaJsonProducer kafkaJsonProducer;

    @CacheEvict(cacheNames = {"feedbacksByProduct", "products", "productsByCategory", "productsByName", "productsWithFeedbacks"}, allEntries = true)
    @Override
    public FeedbackDTO saveFeedback(FeedbackRequestDTO request, Authentication connectedUser) {
        UserWithRoleDTO userDTO = userService.getUserByAuth(connectedUser);

        kafkaJsonProducer.sendMessageToIncrementCustomerFeedbackCount(1);

        Feedback feedback = toFeedbackWithCreator(request, userDTO);
        return toFeedbackDTO(feedbackRepository.save(feedback));
    }

    @Cacheable(cacheNames = "feedbacksByProduct", keyGenerator = "customKeyGenerator")
    @Override
    public Page<FeedbackResponseDTO> getFeedbacksByProduct(Long productId,
                                                           PaginationParams paginationParams,
                                                           Authentication connectedUser) {
        Pageable pageable = pageableBuilder.buildPageable(paginationParams);
        Integer userId = connectedUser != null ? toUser(userService.getUserByAuth(connectedUser)).getId() : 0;
        Page<Feedback> feedbacks = feedbackRepository.findAllByProductId(productId, pageable);


        return feedbackMapper.toFeedbackResponses(feedbacks, userId);
    }
}
