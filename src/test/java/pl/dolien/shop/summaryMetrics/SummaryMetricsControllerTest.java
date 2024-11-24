package pl.dolien.shop.summaryMetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SummaryMetricsControllerTest {

    @InjectMocks
    private SummaryMetricsController summaryMetricsController;

    @Mock
    private SummaryMetricsService summaryMetricsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SummaryMetrics testSummaryMetrics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(summaryMetricsController).build();

        initializeTestData();
    }

    @Test
    void shouldReturnSummaryMetrics() throws Exception {
        when(summaryMetricsService.getSummaryMetrics()).thenReturn(testSummaryMetrics);

        performGetSummaryMetricsRequest()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testSummaryMetrics)));

        verify(summaryMetricsService, times(1)).getSummaryMetrics();
    }

    private void initializeTestData() {
        testSummaryMetrics = SummaryMetrics.builder()
                .id(1L)
                .totalUsers(0)
                .totalOrders(0)
                .totalCustomerFeedback(0)
                .totalProductsSold(0)
                .revenue(BigDecimal.ZERO)
                .build();
    }

    private ResultActions performGetSummaryMetricsRequest() throws Exception {
        return mockMvc.perform(get("/summary-metrics")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSummaryMetrics)));
    }
}