package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ProductNotFoundException;
import pl.dolien.shop.file.FileService;
import pl.dolien.shop.image.ImageUploader;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.pagination.PageableBuilder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final PageableBuilder pageableBuilder;
    private final ProductRepository productRepository;
    public final ProductMapper productMapper;
    private final ImageUploader imageUploader;
    private final SkuGenerator skuGenerator;
    private final FileService fileService;

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

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public void saveProductWithImage(ProductDTO productDTO, MultipartFile file) {
        fileService.validateFileIsNotEmpty(file);

        String imageUrl = imageUploader.uploadImage(file);
        Product product = productMapper.toProduct(productDTO, imageUrl);

        Product savedProduct = productRepository.save(product);

        String sku = skuGenerator.generateSku(savedProduct.getName(), savedProduct.getId());
        savedProduct.setSku(sku);
        productRepository.save(savedProduct);
    }
}
