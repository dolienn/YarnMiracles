package pl.dolien.shop.summaryMetrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.exception.SummaryMetricsNotFoundException;
import pl.dolien.shop.order.Order;

import java.math.BigDecimal;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummaryMetricsServiceTest {

    private static final String SUMMARY_METRICS_NOT_FOUND_MESSAGE = "Summary metrics not found";

    @InjectMocks
    private SummaryMetricsService summaryMetricsService;

    @Mock
    private SummaryMetricsRepository summaryMetricsRepository;

    private SummaryMetrics testSummaryMetrics;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldReturnSummaryMetrics() {
        when(summaryMetricsRepository.findById(1L)).thenReturn(of(testSummaryMetrics));

        SummaryMetrics result = summaryMetricsService.getSummaryMetrics();

        assertEquals(testSummaryMetrics, result);
        verify(summaryMetricsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenSummaryMetricsNotFound() {
        when(summaryMetricsRepository.findById(1L)).thenReturn(empty());

        SummaryMetricsNotFoundException exception = assertThrows(
                SummaryMetricsNotFoundException.class,
                () -> summaryMetricsService.getSummaryMetrics()
        );

        assertEquals(SUMMARY_METRICS_NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldUpdateOrderMetrics() {
        when(summaryMetricsRepository.findById(1L)).thenReturn(of(testSummaryMetrics));

        summaryMetricsService.updateOrderMetrics(testOrder);

        assertUpdatedOrderMetrics();
        verify(summaryMetricsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldIncrementUserCount() {
        when(summaryMetricsRepository.findById(1L)).thenReturn(of(testSummaryMetrics));

        summaryMetricsService.incrementUserCount(1);

        assertEquals(1, testSummaryMetrics.getTotalUsers());
        verify(summaryMetricsRepository, times(1)).findById(1L);
    }

    @Test
    void shouldIncrementCustomerFeedbackCount() {
        when(summaryMetricsRepository.findById(1L)).thenReturn(of(testSummaryMetrics));

        summaryMetricsService.incrementCustomerFeedbackCount(1);

        assertEquals(1, testSummaryMetrics.getTotalCustomerFeedback());
        verify(summaryMetricsRepository, times(1)).findById(1L);
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

        testOrder = Order.builder()
                .totalPrice(BigDecimal.TEN)
                .totalQuantity(1)
                .build();
    }

    private void assertUpdatedOrderMetrics() {
        assertEquals(1, testSummaryMetrics.getTotalOrders());
        assertEquals(BigDecimal.TEN, testSummaryMetrics.getRevenue());
        assertEquals(1, testSummaryMetrics.getTotalProductsSold());
    }
}