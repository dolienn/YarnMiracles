package pl.dolien.shop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.kafka.producer.KafkaJsonProducer;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaJsonProducer kafkaJsonProducer;

    private Order order;
    private Set<OrderItem> orderItems;
    private PurchaseRequestDTO purchaseRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldCreateOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order response = orderService.buildOrder(purchaseRequestDTO);

        assertOrder(response, purchaseRequestDTO);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void shouldAddOrderItems() {
        orderService.addOrderItems(order, orderItems);
    }

    private void initializeTestData() {
        order = Order.builder().build();

        orderItems = Set.of(OrderItem.builder().build());

        purchaseRequestDTO = PurchaseRequestDTO.builder()
                .order(order)
                .orderItems(orderItems)
                .build();
    }

    private void assertOrder(Order result, PurchaseRequestDTO purchaseRequestDTO) {
        assertNotNull(result);
        assertEquals(order.getOrderTrackingNumber(), result.getOrderTrackingNumber());
        assertEquals(purchaseRequestDTO.getShippingAddress(), result.getShippingAddress());
        assertEquals(purchaseRequestDTO.getBillingAddress(), result.getBillingAddress());
    }
}
