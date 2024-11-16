package pl.dolien.shop.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.checkout.dto.PurchaseResponseDTO;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.customer.CustomerService;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderService;
import pl.dolien.shop.summaryMetrics.SummaryMetricsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    @InjectMocks
    private CheckoutService checkoutService;

    @Mock
    private OrderService orderService;

    @Mock
    private CustomerService customerService;

    @Mock
    private SummaryMetricsService dashboardService;

    @Mock
    private Authentication authentication;

    private PurchaseRequestDTO testPurchaseRequestDTO;
    private Order testOrder;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldPlaceOrder() {
        mockDependenciesForPlaceOrder();

        PurchaseResponseDTO response = checkoutService.placeOrder(testPurchaseRequestDTO, authentication);

        assertPurchaseResponse(response);
        verifyInteractionsForPlaceOrder();
    }

    private void initializeTestData() {
        testPurchaseRequestDTO = PurchaseRequestDTO.builder()
                .build();

        testOrder = Order.builder()
                .id(1L)
                .orderTrackingNumber("trackingNumber")
                .build();

        testCustomer = Customer.builder()
                .id(1L)
                .build();
    }

    private void mockDependenciesForPlaceOrder() {
        when(orderService.buildOrder(testPurchaseRequestDTO)).thenReturn(testOrder);
        when(customerService.processCustomer(testOrder, testPurchaseRequestDTO.getCustomer(), authentication))
                .thenReturn(testCustomer);
    }

    private void assertPurchaseResponse(PurchaseResponseDTO response) {
        assertNotNull(response);
        assertEquals(testOrder.getOrderTrackingNumber(), response.getOrderTrackingNumber());
    }

    private void verifyInteractionsForPlaceOrder() {
        verify(orderService, times(1)).buildOrder(testPurchaseRequestDTO);
        verify(customerService, times(1)).processCustomer(testOrder, testPurchaseRequestDTO.getCustomer(), authentication);
        verify(dashboardService, times(1)).updateOrderMetrics(testOrder);
    }
}