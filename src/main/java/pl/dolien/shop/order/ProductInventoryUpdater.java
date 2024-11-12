package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;

@Service
@RequiredArgsConstructor
public class ProductInventoryUpdater {
    private final ProductService productService;
    private final KafkaTemplate<String, Integer> kafkaTemplate;

    @KafkaListener(topics = "inventory-update", groupId = "inventory-updater")
    @Transactional
    public void updateProductInventory(OrderItem orderItem) {
        Product product = productService.getProductById(orderItem.getProductId());
        product.incrementSales(orderItem.getQuantity());

        if (product.getUnitsInStock() >= orderItem.getQuantity()) {
            product.removeUnitsInStock(orderItem.getQuantity());
        }

        productService.saveProduct(product);

        kafkaTemplate.send("dashboard-sales-update", orderItem.getQuantity());
    }
}
