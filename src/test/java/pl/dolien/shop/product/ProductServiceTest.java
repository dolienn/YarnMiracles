package pl.dolien.shop.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.feedback.*;
import pl.dolien.shop.file.FileValidator;
import pl.dolien.shop.image.ImageUploader;
import pl.dolien.shop.pagination.*;
import pl.dolien.shop.product.dto.*;
import pl.dolien.shop.productCategory.ProductCategory;
import pl.dolien.shop.user.UserService;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private static final String PRODUCT_NAME = "Test product";

    @InjectMocks
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private FileValidator fileValidator;

    @Mock
    private ImageUploader imageUploader;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private PageableBuilder pageableBuilder;

    @Mock
    private Pageable pageable;

    private Product testProduct;
    private ProductRequestDTO testProductRequestDTO;
    private Feedback testFeedback;
    private PaginationAndSortParams testPaginationAndSortParams;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldGetProductById() {
        when(productRepository.findById(testProduct.getId())).thenReturn(of(testProduct));

        Product product = productService.getProductById(testProduct.getId());

        assertEquals(testProduct, product);
        verify(productRepository, times(1)).findById(testProduct.getId());
    }

    @Test
    void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> products = productService.getAllProducts();

        assertAllProducts(products);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllProductsWithPagination() {
        mockPageableBuilder();
        when(productRepository.findAllProducts(pageable)).thenReturn(buildRestPage(List.of(testProduct)));

        Page<ProductDTO> productDTOs = productService.getAllProducts(testPaginationAndSortParams);

        assertProducts(productDTOs);
        verifyPageableBuilder();
        verify(productRepository, times(1)).findAllProducts(pageable);
    }

    @Test
    void shouldGetProductsByCategoryId() {
        mockPageableBuilder();
        when(productRepository.findByCategoryId(testProduct.getCategoryId(), pageable)).thenReturn(buildRestPage(List.of(testProduct)));

        Page<ProductDTO> productDTOs = productService.getProductsByCategoryId(testProduct.getCategoryId(), testPaginationAndSortParams);

        assertProducts(productDTOs);
        verifyPageableBuilder();
        verify(productRepository, times(1)).findByCategoryId(testProduct.getCategoryId(), pageable);
    }

    @Test
    void shouldGetProductsByKeyword() {
        mockPageableBuilder();
        when(productRepository.findByNameContaining(testProduct.getName(), pageable)).thenReturn(buildRestPage(List.of(testProduct)));

        Page<ProductDTO> productDTOs = productService.getProductsByKeyword(testProduct.getName(), testPaginationAndSortParams);

        assertProducts(productDTOs);
        verifyPageableBuilder();
        verify(productRepository, times(1)).findByNameContaining(testProduct.getName(), pageable);
    }

    @Test
    void shouldGetAllProductsWithFeedbacks() {
        mockPageableBuilder();
        mockProductsWithFeedbacks();

        Page<ProductWithFeedbackDTO> productWithFeedbackDTOs = productService.getAllProductsWithFeedbacks(testPaginationAndSortParams);

        assertProductsWithFeedbacks(productWithFeedbackDTOs);
        verifyPageableBuilder();
        verifyProductsWithFeedbacks();
    }

    @Test
    void shouldSaveProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product savedProduct = productService.saveProduct(testProduct);

        assertEquals(testProduct, savedProduct);
        verifySaveProduct();
    }

    @Test
    void ShouldSaveProductWithImage() {
        mockSaveProductWithImage();

        ProductDTO response = productService.saveProductWithImage(testProductRequestDTO, file, authentication);

        assertSavedProductWithImage(response);
        verifySaveProductWithImage();
    }

    private void initializeTestData() {
        testProduct = Product.builder()
                .id(1L)
                .categoryId(1)
                .name(PRODUCT_NAME)
                .build();

        testProductRequestDTO = ProductRequestDTO.builder()
                .category(ProductCategory.builder().build())
                .name(PRODUCT_NAME)
                .description("test description")
                .unitPrice(BigDecimal.TEN)
                .unitsInStock(10)
                .build();

        testFeedback = Feedback.builder()
                .id(1)
                .productId(1L)
                .build();

        testPaginationAndSortParams = PaginationAndSortParams.builder()
                .page(1)
                .size(10)
                .build();

        file = mock(MultipartFile.class);
    }

    private void mockPageableBuilder() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
    }

    private void mockProductsWithFeedbacks() {
        when(productRepository.findAllProducts(pageable)).thenReturn(buildRestPage(List.of(testProduct)));
        when(feedbackRepository.findAllByProductIdIn(List.of(testProduct.getId()))).thenReturn(Collections.singletonList(testFeedback));
    }

    private void mockSaveProductWithImage() {
        when(imageUploader.uploadImage(file)).thenReturn("testUrl");
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
    }

    private void assertAllProducts(List<Product> products) {
        assertEquals(1, products.size());
        assertEquals(testProduct, products.get(0));
    }

    private void assertProducts(Page<ProductDTO> productDTOs) {
        assertEquals(1, productDTOs.getContent().size());
        assertEquals(testProduct.getId(), productDTOs.getContent().get(0).getId());
        assertEquals(testProduct.getName(), productDTOs.getContent().get(0).getName());
    }

    private void assertProductsWithFeedbacks(Page<ProductWithFeedbackDTO> productWithFeedbackDTOs) {
        assertEquals(1, productWithFeedbackDTOs.getContent().size());
        assertEquals(testProduct.getId(), productWithFeedbackDTOs.getContent().get(0).getId());
        assertEquals(testProduct.getName(), productWithFeedbackDTOs.getContent().get(0).getName());
        assertEquals(1, productWithFeedbackDTOs.getContent().get(0).getFeedbacks().size());
        assertEquals(testFeedback.getId(), productWithFeedbackDTOs.getContent().get(0).getFeedbacks().get(0).getId());
        assertEquals(testFeedback.getProductId(), productWithFeedbackDTOs.getContent().get(0).getFeedbacks().get(0).getProductId());
    }

    private void assertSavedProductWithImage(ProductDTO result) {
        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getImageUrl(), result.getImageUrl());
    }

    private void verifyPageableBuilder() {
        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
    }

    private void verifyProductsWithFeedbacks() {
        verify(productRepository, times(1)).findAllProducts(pageable);
        verify(feedbackRepository, times(1)).findAllByProductIdIn(List.of(testProduct.getId()));
    }

    private void verifySaveProductWithImage() {
        verify(userService, times(1)).verifyUserHasAdminRole(authentication);
        verify(fileValidator, times(1)).validateFileIsNotEmpty(file);
        verify(imageUploader, times(1)).uploadImage(file);
        verifySaveProduct();
    }

    private void verifySaveProduct() {
        verify(productRepository, times(1)).save(testProduct);
    }

    private RestPage<Product> buildRestPage(List<Product> content) {
        return new RestPage<>(content, 1, 1, 1);
    }
}
