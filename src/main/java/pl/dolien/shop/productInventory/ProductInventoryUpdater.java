package pl.dolien.shop.productInventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.exception.NotEnoughStockException;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;

@Component
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
