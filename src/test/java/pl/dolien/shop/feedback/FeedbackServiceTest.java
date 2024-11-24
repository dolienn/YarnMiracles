package pl.dolien.shop.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.summaryMetrics.SummaryMetricsService;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationParams;
import pl.dolien.shop.user.UserService;
import pl.dolien.shop.user.dto.UserWithRoleDTO;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @Mock
    private UserService userService;

    @Mock
    private SummaryMetricsService dashboardService;

    @Mock
    private PageableBuilder pageableBuilder;

    @Mock
    private Authentication authentication;

    private UserWithRoleDTO testUserDTO;
    private Feedback testFeedback;
    private FeedbackDTO testFeedbackDTO;
    private FeedbackRequestDTO testFeedbackRequestDTO;
    private FeedbackResponseDTO testFeedbackResponseDTO;
    private PaginationParams testPaginationParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldSaveFeedback() {
        mockDependenciesForSaveFeedback();

        FeedbackDTO result = feedbackService.saveFeedback(testFeedbackRequestDTO, authentication);

        assertEquals(testFeedbackDTO.getId(), result.getId());

        verifyInteractionsForSaveFeedback();
    }

    @Test
    void shouldGetFeedbacksByProduct() {
        mockDependenciesForGetFeedbacksByProduct();

        Page<FeedbackResponseDTO> result = feedbackService.getFeedbacksByProduct(1L, testPaginationParams, authentication);

        assertEquals(1, result.getContent().size());
        assertEquals(testFeedback.getCreatedBy(), result.getContent().get(0).getCreatedBy().getId());

        verifyInteractionsForGetFeedbacksByProduct();
    }

    private void initializeTestData() {
        testUserDTO = UserWithRoleDTO.builder()
                .id(1)
                .firstname("John")
                .lastname("Doe")
                .email("test@example")
                .dateOfBirth(parse("2020-01-01"))
                .accountLocked(false)
                .enabled(true)
                .createdDate(parse("2020-01-01").atStartOfDay())
                .lastModifiedDate(parse("2020-01-01").atStartOfDay())
                .build();

        testFeedback = Feedback.builder()
                .id(1)
                .createdBy(1)
                .build();

        testFeedbackDTO = FeedbackDTO.builder()
                .id(1)
                .build();

        testFeedbackRequestDTO = FeedbackRequestDTO.builder()
                .note(5D)
                .comment("testFeedback")
                .productId(1L)
                .build();

        testFeedbackResponseDTO = FeedbackResponseDTO.builder()
                .createdBy(testUserDTO)
                .build();

        testPaginationParams = PaginationParams.builder()
                .page(1)
                .size(10)
                .build();
    }

    private void mockDependenciesForSaveFeedback() {
        when(userService.getUserByAuth(authentication)).thenReturn(testUserDTO);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(testFeedback);
    }

    private void verifyInteractionsForSaveFeedback() {
        verify(userService, times(1)).getUserByAuth(authentication);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    private void mockDependenciesForGetFeedbacksByProduct() {
        when(userService.getUserByAuth(any(Authentication.class))).thenReturn(testUserDTO);
        when(feedbackMapper.toFeedbackResponses(any(Page.class), anyInt())).thenReturn(buildRestPageByFeedbackResponseDTO(List.of(testFeedbackResponseDTO)));
        when(pageableBuilder.buildPageable(testPaginationParams)).thenReturn(mock(Pageable.class));
        when(feedbackRepository.findAllByProductId(anyLong(), any(Pageable.class)))
                .thenReturn(buildRestPageByFeedback(List.of(testFeedback)));
    }

    private void verifyInteractionsForGetFeedbacksByProduct() {
        verify(userService, times(1)).getUserByAuth(any(Authentication.class));
        verify(pageableBuilder, times(1)).buildPageable(testPaginationParams);
        verify(feedbackRepository, times(1)).findAllByProductId(anyLong(), any(Pageable.class));
    }

    private Page<Feedback> buildRestPageByFeedback(List<Feedback> content) {
        return new RestPage<>(content, 1, 1, 1);
    }

    private Page<FeedbackResponseDTO> buildRestPageByFeedbackResponseDTO(List<FeedbackResponseDTO> testFeedbackResponseDTO) {
        return new RestPage<>(testFeedbackResponseDTO, 1, 1, 1);
    }
}
