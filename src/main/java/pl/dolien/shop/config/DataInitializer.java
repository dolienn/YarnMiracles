package pl.dolien.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.user.UserService;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        productService.updateProductImages();
        userService.addAdminUser();
    }
}