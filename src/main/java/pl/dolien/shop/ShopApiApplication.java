package pl.dolien.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApiApplication.class, args);
	}

}
