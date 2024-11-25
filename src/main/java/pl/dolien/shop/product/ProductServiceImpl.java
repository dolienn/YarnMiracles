package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ProductNotFoundException;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.feedback.FeedbackRepository;
import pl.dolien.shop.file.FileValidator;
import pl.dolien.shop.image.ImageUploader;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;
import pl.dolien.shop.user.UserService;

import java.util.List;

import static pl.dolien.shop.product.ProductMapper.*;
import static pl.dolien.shop.product.SkuGenerator.generateSku;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final PageableBuilder pageableBuilder;
    private final ProductRepository productRepository;
    private final ImageUploader imageUploader;
    private final FileValidator fileValidator;
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable(cacheNames = "products", keyGenerator = "customKeyGenerator")
    @Override
    public Page<ProductDTO> getAllProducts(PaginationAndSortParams paginationAndSortParams) {
        Pageable pageable = pageableBuilder.buildPageable(paginationAndSortParams);

        return toProductDTOs(productRepository.findAllProducts(pageable));
    }

    @Cacheable(cacheNames = "productsByCategory", keyGenerator = "customKeyGenerator")
    @Override
    public Page<ProductDTO> getProductsByCategoryId(Integer categoryId, PaginationAndSortParams paginationAndSortParams) {
        Pageable pageable = pageableBuilder.buildPageable(paginationAndSortParams);

        return toProductDTOs(productRepository.findByCategoryId(categoryId, pageable));
    }

    @Cacheable(cacheNames = "productsByName", keyGenerator = "customKeyGenerator")
    @Override
    public Page<ProductDTO> getProductsByKeyword(String keyword, PaginationAndSortParams paginationAndSortParams) {
        Pageable pageable = pageableBuilder.buildPageable(paginationAndSortParams);

        return toProductDTOs(productRepository.findByNameContaining(keyword, pageable));
    }

    @Cacheable(cacheNames = "productsWithFeedbacks", keyGenerator = "customKeyGenerator")
    @Override
    public Page<ProductWithFeedbackDTO> getAllProductsWithFeedbacks(PaginationAndSortParams paginationAndSortParams) {
        Page<ProductDTO> productDTOs = getAllProducts(paginationAndSortParams);

        List<Long> productIds = productDTOs.stream()
                .map(ProductDTO::getId)
                .toList();
        List<Feedback> feedbacks = feedbackRepository.findAllByProductIdIn(productIds);

        return toProductWithFeedbackDTOs(productDTOs, feedbacks);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(
            cacheNames = {"products", "productsByCategory", "productsByName", "productsWithFeedbacks"},
            allEntries = true
    )
    @Override
    public ProductDTO saveProductWithImage(ProductRequestDTO request, MultipartFile file, Authentication connectedUser) {
        userService.verifyUserHasAdminRole(connectedUser);

        fileValidator.validateFileIsNotEmpty(file);
        fileValidator.validateUploadDirectory();

        String imageUrl = imageUploader.uploadImage(file);
        Product product = toProduct(request, imageUrl);

        Product savedProduct = saveProduct(product);

        String sku = generateSku(savedProduct.getName(), savedProduct.getId());
        savedProduct.setSku(sku);

        return toProductDTO(saveProduct(savedProduct));
    }
}
