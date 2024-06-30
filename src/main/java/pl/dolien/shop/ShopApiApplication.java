package pl.dolien.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.feedback.FeedbackRepository;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductCategoryRepository;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.role.Role;
import pl.dolien.shop.role.RoleRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableJpaAuditing()
//@EnableJpaRepositories(basePackages = "pl.dolien.shop")
//@EntityScan(basePackages = {"pl.dolien.shop.user", "pl.dolien.shop.role", "pl.dolien.shop.product"})
@EnableAsync
public class ShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository, ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, FeedbackRepository feedbackRepository) {
		return args -> {
//			Feedback feedback = feedbackRepository.findById(998).get();
//			feedback.setNote(5.0);
//			feedbackRepository.save(feedback);

			if(roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}

			if(productRepository.findByName("Urban shirt").isEmpty()) {

//				productRepository.save(
//						Product.builder()
//								.sku("4324213421")
//								.active(true)
//								.category(productCategoryRepository.findByCategoryName("Shirts").get())
//								.name("Night shirt")
//								.description("xppppp")
//								.imageUrl("/images/products/product2.jpg")
//								.unitsInStock(100)
//								.unitPrice(BigDecimal.valueOf(15.99))
//								.lowestPriceWithin30Days(BigDecimal.valueOf(10.99))
//								.build()
//				);
//
//				productRepository.save(
//						Product.builder()
//								.sku("54351341")
//								.active(true)
//								.category(productCategoryRepository.findByCategoryName("Shirts").get())
//								.name("Classic shirt")
//								.description("djkiasdjidasjd")
//								.imageUrl("/images/products/product3.jpg")
//								.unitsInStock(100)
//								.unitPrice(BigDecimal.valueOf(25.99))
//								.lowestPriceWithin30Days(BigDecimal.valueOf(20.99))
//								.build()
//				);

				productRepository.save(
						Product.builder()
								.sku("412421412312")
								.active(true)
								.category(productCategoryRepository.findByCategoryName("Shirts").get())
								.name("Urban shirt")
								.description("dassdkamdakldmaklsmdkla")
								.imageUrl("/images/products/product4.jpg")
								.unitsInStock(100)
								.unitPrice(BigDecimal.valueOf(10.99))
								.lowestPriceWithin30Days(BigDecimal.valueOf(9.99))
								.build()
				);

				productRepository.save(
						Product.builder()
								.sku("412312313")
								.active(true)
								.category(productCategoryRepository.findByCategoryName("Shirts").get())
								.name("Bershka shirt")
								.description("dassdkamdakldmaklsmdkla")
								.imageUrl("/images/products/product5.jpg")
								.unitsInStock(100)
								.unitPrice(BigDecimal.valueOf(20.99))
								.lowestPriceWithin30Days(BigDecimal.valueOf(15.99))
								.build()
				);

				Set<Product> products = new HashSet<>();
				products.add(productRepository.findByName("Oversized fit shirt").get());
				products.add(productRepository.findByName("Night shirt").get());
				products.add(productRepository.findByName("Classic shirt").get());
				products.add(productRepository.findByName("Urban shirt").get());
				products.add(productRepository.findByName("Bershka shirt").get());
				productCategoryRepository.findByCategoryName("Shirts").get().setProducts(products);
			}
		};
	}
}
