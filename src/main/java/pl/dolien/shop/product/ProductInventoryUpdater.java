package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.exception.NotEnoughStockException;
import pl.dolien.shop.order.OrderItem;

@Service
@RequiredArgsConstructor
public class ProductInventoryUpdater {
    private final ProductService productService;

    @Transactional
    public void updateProductInventory(OrderItem orderItem) {
        Product product = productService.getProductById(orderItem.getProductId());
        product.incrementSales(orderItem.getQuantity());

        if (product.getUnitsInStock() >= orderItem.getQuantity()) {
            product.removeUnitsInStock(orderItem.getQuantity());
        } else {
            throw new NotEnoughStockException("Not enough units in stock for product: " + product.getId());
        }
    }
}
