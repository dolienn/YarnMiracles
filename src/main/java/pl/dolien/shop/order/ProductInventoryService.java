package pl.dolien.shop.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.dashboard.DashboardService;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;

@Service
@RequiredArgsConstructor
public class ProductInventoryService {
    private final ProductService productService;
    private final DashboardService dashboardService;

    @Transactional
    public void updateProductInventory(OrderItem orderItem) {
        Product product = productService.getProductById(orderItem.getProductId());
        product.incrementSales(orderItem.getQuantity());

        if (product.getUnitsInStock() >= orderItem.getQuantity()) {
            product.removeUnitsInStock(orderItem.getQuantity());
        }

        dashboardService.updateProductSales(orderItem.getQuantity());
        productService.saveProduct(product);
    }
}
