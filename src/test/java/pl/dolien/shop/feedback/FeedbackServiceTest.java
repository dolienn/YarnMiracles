package pl.dolien.shop.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.summaryMetrics.SummaryMetricsService;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationParams;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserDTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserService userService;

    @Mock
    private SummaryMetricsService dashboardService;

    @Mock
    private PageableBuilder pageableBuilder;

    @Mock
    private Authentication authentication;

    private User testUser;
    private UserDTO testUserDTO;
    private Feedback testFeedback;
    private FeedbackDTO testFeedbackDTO;
    private FeedbackRequestDTO testFeedbackRequestDTO;
    private PaginationParams testPaginationParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldSaveFeedback() {
        when(userService.getUserDTOByAuth(authentication)).thenReturn(testUserDTO);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(testFeedback);

        FeedbackDTO result = feedbackService.saveFeedback(testFeedbackRequestDTO, authentication);

        assertEquals(testFeedbackDTO.getId(), result.getId());

        verify(userService, times(1)).getUserDTOByAuth(authentication);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void shouldGetFeedbacksByProduct() {
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(pageableBuilder.buildPageable(testPaginationParams)).thenReturn(mock(Pageable.class));
        when(feedbackRepository.findAllByProductId(anyLong(), any(Pageable.class)))
                .thenReturn(Collections.singletonList(testFeedback));

        List<FeedbackResponseDTO> result = feedbackService.getFeedbacksByProduct(1L, testPaginationParams, authentication);

        assertEquals(1, result.size());
        assertEquals(testFeedback.getCreatedBy(), result.get(0).getCreatedBy());

        verify(authentication, times(1)).getPrincipal();
        verify(pageableBuilder, times(1)).buildPageable(testPaginationParams);
        verify(feedbackRepository, times(1)).findAllByProductId(anyLong(), any(Pageable.class));
    }

    private void initializeTestData() {
        testUser = User.builder()
                .id(4)
                .build();

        testUserDTO = UserDTO.builder()
                .id(1)
                .build();

        testFeedback = Feedback.builder()
                .id(1)
                .build();

        testFeedbackDTO = FeedbackDTO.builder()
                .id(1)
                .build();

        testFeedbackRequestDTO = FeedbackRequestDTO.builder()
                .note(5D)
                .comment("testFeedback")
                .productId(1L)
                .build();

        testPaginationParams = PaginationParams.builder()
                .page(1)
                .size(10)
                .build();
    }
}
