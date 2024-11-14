package pl.dolien.shop.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PaginationParams;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FeedbackControllerTest {

    @InjectMocks
    private FeedbackController feedbackController;

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private FeedbackDTO testFeedbackDTO;
    private FeedbackRequestDTO testFeedbackRequestDTO;
    private FeedbackResponseDTO testFeedbackResponseDTO;
    private PaginationParams testPaginationParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(feedbackController).build();
        initializeTestData();
    }

    @Test
    void shouldSaveFeedback() throws Exception {
        when(feedbackService.saveFeedback(testFeedbackRequestDTO, authentication)).thenReturn(testFeedbackDTO);

        mockMvc.perform(post("/feedbacks")
                        .content(objectMapper.writeValueAsString(testFeedbackRequestDTO))
                        .principal(authentication)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testFeedbackDTO)));

        verify(feedbackService).saveFeedback(testFeedbackRequestDTO, authentication);
    }

    @Test
    void shouldGetAllFeedbacksByProduct() throws Exception {
        when(feedbackService.getFeedbacksByProduct(anyLong(), any(PaginationParams.class), any(Authentication.class)))
                .thenReturn(List.of(testFeedbackResponseDTO));

        mockMvc.perform(get("/feedbacks/products/{productId}", 1L)
                        .content(objectMapper.writeValueAsString(testPaginationParams))
                        .contentType(APPLICATION_JSON)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(testFeedbackResponseDTO))));

        verify(feedbackService).getFeedbacksByProduct(anyLong(), any(PaginationParams.class), any(Authentication.class));
    }

    private void initializeTestData() {
        testFeedbackDTO = FeedbackDTO.builder()
                .id(1)
                .build();

        testFeedbackRequestDTO = FeedbackRequestDTO.builder()
                .note(5D)
                .comment("Test Feedback")
                .productId(1L)
                .build();

        testFeedbackResponseDTO = FeedbackResponseDTO.builder()
                .createdBy(1)
                .build();

        testPaginationParams = PaginationParams.builder()
                .page(0)
                .size(10)
                .build();
    }
}
