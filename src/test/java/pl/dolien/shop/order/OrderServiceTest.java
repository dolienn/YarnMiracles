package pl.dolien.shop.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.product.ProductInventoryUpdater;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private ProductInventoryUpdater productInventoryService;

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

        Order response = orderService.buildOrder(purchaseRequestDTO);

        assertOrder(response, purchaseRequestDTO);
    }

    @Test
    void shouldAddOrderItems() {
        orderService.addOrderItems(order, orderItems);

        orderItems.forEach(item -> verify(productInventoryService, times(1)).updateProductInventory(item));
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
