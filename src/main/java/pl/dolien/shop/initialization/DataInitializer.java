package pl.dolien.shop.initialization;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductImageInitializer productImageInitializer;
    private final AdminUserInitializer adminUserInitializer;

    @Override
    public void run(String... args) throws Exception {
        productImageInitializer.updateAllProductImages();
        adminUserInitializer.initializeAdminUser();
    }
}