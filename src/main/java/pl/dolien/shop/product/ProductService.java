package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductNotFoundException;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.sort.SortService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final SortService sortService;
    private final PageableBuilder pageableBuilder;
    private final ProductRepository productRepository;

    public Page<Product> getAllProducts(PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategoryId(Long categoryId, PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> getProductsByNameContaining(String name, PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findByNameContaining(name, pageable);
    }

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
