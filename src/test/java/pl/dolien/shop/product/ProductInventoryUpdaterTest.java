package pl.dolien.shop.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.order.OrderItem;

import static org.mockito.Mockito.*;

class ProductInventoryUpdaterTest {

    @InjectMocks
    private ProductInventoryUpdater updater;

    @Mock
    private ProductService productService;

    private OrderItem orderItem;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    public void testUpdateProductInventory_Success() {
        when(productService.getProductById(orderItem.getProductId())).thenReturn(product);

        updater.updateProductInventory(orderItem);

        verify(productService, times(1)).getProductById(orderItem.getProductId());
    }

    private void initializeTestData() {
        orderItem = OrderItem.builder().build();
        product = Product.builder().build();
    }
}
