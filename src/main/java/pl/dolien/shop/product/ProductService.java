package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.exception.ProductNotFoundException;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.feedback.FeedbackRepository;
import pl.dolien.shop.file.FileService;
import pl.dolien.shop.image.ImageUploader;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.user.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.dolien.shop.product.ProductMapper.toProduct;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final PageableBuilder pageableBuilder;
    private final ProductRepository productRepository;
    private final ImageUploader imageUploader;
    private final SkuGenerator skuGenerator;
    private final FileService fileService;
    private final FeedbackRepository feedbackRepository;
    private final UserService userService;

    public List<Product> getAllProducts(PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findAllProducts(pageable);
    }

    public List<Product> getProductsByCategoryId(Long categoryId, PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public List<Product> getProductsByNameContaining(String name, PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return productRepository.findByNameContaining(name, pageable);
    }

    public List<Product> getProductsWithFeedbacks(PageRequestParams pageRequestParams) {
        List<Product> products = getAllProducts(pageRequestParams);
        List<Long> ids = products.stream()
                .map(Product::getId)
                .toList();
        List<Feedback> feedbacks = feedbackRepository.findAllByProductIdIn(ids);
        products.forEach(product -> product.setFeedbacks(extractFeedbacks(feedbacks, product.getId())));

        return products;
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product saveProductWithImage(ProductRequestDTO request, MultipartFile file, Authentication connectedUser) {
        userService.verifyUserHasAdminRole(connectedUser);

        fileService.validateFileIsNotEmpty(file);
        fileService.validateUploadDirectory();

        String imageUrl = imageUploader.uploadImage(file);
        Product product = toProduct(request, imageUrl);

        Product savedProduct = saveProduct(product);

        String sku = skuGenerator.generateSku(savedProduct.getName(), savedProduct.getId());
        savedProduct.setSku(sku);

        return saveProduct(savedProduct);
    }

    private List<Feedback> extractFeedbacks(List<Feedback> feedbacks, Long productId) {
        return feedbacks.stream()
                .filter(feedback -> Objects.equals(feedback.getProductId(), productId))
                .collect(Collectors.toList());
    }
}
