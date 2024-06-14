package pl.dolien.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductCategory;
import pl.dolien.shop.product.ProductCategoryRepository;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "pl.dolien.shop")
//@EntityScan(basePackages = {"pl.dolien.shop.user", "pl.dolien.shop.role", "pl.dolien.shop.product"})
@EnableAsync
public class ShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository, ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}

			if(productCategoryRepository.findByCategoryName("Shirts").isEmpty()) {
				productCategoryRepository.save(
						ProductCategory.builder().categoryName("Shirts").build()
				);

				productRepository.save(
						Product.builder()
								.sku("31241232")
								.active(true)
								.category(productCategoryRepository.findByCategoryName("Shirts").get())
								.name("Oversized fit shirt")
								.description("xddd")
								.imageUrl("/images/products/product1.jpg")
								.unitsInStock(100)
								.unitPrice(BigDecimal.valueOf(12.99))
								.lowestPriceWithin30Days(BigDecimal.valueOf(12.99))
								.build()
				);

				Set<Product> products = new HashSet<>();
				products.add(productRepository.findByName("Oversized fit shirt").get());
				productCategoryRepository.findByCategoryName("Shirts").get().setProducts(products);
			} else {
				System.out.println(productCategoryRepository.findByCategoryName("Shirts").get().getCategoryName());
			}
		};
	}
}
