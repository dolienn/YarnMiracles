package pl.dolien.shop.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.dolien.shop.product.ProductService;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;
    private final AdminUserInitializer adminUserInitializer;

    @Override
    public void run(String... args) throws Exception {
        productService.updateProductImages();
        adminUserInitializer.initializeAdminUser();
    }
}