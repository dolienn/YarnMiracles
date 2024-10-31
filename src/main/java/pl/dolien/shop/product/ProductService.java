package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String generateSku(String productName, Long productId) {
        String namePart = productName.substring(0, Math.min(3, productName.length())).toUpperCase();
        String idPart = String.format("%04d", productId);
        return namePart + "-" + idPart;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
}
