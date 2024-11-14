package pl.dolien.shop.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        when(productRepository.findById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        Product product = productService.getProductById(testProduct.getId());

        assertEquals(testProduct, product);

        verify(productRepository, times(1)).findById(testProduct.getId());
    }

    @Test
    void shouldGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> products = productService.getAllProducts();

        assertEquals(1, products.size());
        assertEquals(testProduct, products.get(0));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllProductsWithPagination() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
        when(productRepository.findAllProducts(pageable)).thenReturn(List.of(testProduct));

        List<ProductDTO> productDTOs = productService.getAllProducts(testPaginationAndSortParams);

        assertEquals(1, productDTOs.size());
        assertEquals(testProduct.getId(), productDTOs.get(0).getId());
        assertEquals(testProduct.getName(), productDTOs.get(0).getName());

        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
        verify(productRepository, times(1)).findAllProducts(pageable);
    }

    @Test
    void shouldGetProductsByCategoryId() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
        when(productRepository.findByCategoryId(testProduct.getCategoryId(), pageable)).thenReturn(List.of(testProduct));

        List<ProductDTO> productDTOs = productService.getProductsByCategoryId(testProduct.getCategoryId(), testPaginationAndSortParams);

        assertEquals(1, productDTOs.size());
        assertEquals(testProduct.getId(), productDTOs.get(0).getId());
        assertEquals(testProduct.getName(), productDTOs.get(0).getName());

        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
        verify(productRepository, times(1)).findByCategoryId(testProduct.getCategoryId(), pageable);
    }

    @Test
    void shouldGetProductsByNameContaining() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
        when(productRepository.findByNameContaining(testProduct.getName(), pageable)).thenReturn(List.of(testProduct));

        List<ProductDTO> productDTOs = productService.getProductsByNameContaining(testProduct.getName(), testPaginationAndSortParams);

        assertEquals(1, productDTOs.size());
        assertEquals(testProduct.getId(), productDTOs.get(0).getId());
        assertEquals(testProduct.getName(), productDTOs.get(0).getName());

        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
        verify(productRepository, times(1)).findByNameContaining(testProduct.getName(), pageable);
    }

    @Test
    void shouldGetAllProductsWithFeedbacks() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
        when(productRepository.findAllProducts(pageable)).thenReturn(List.of(testProduct));
        when(feedbackRepository.findAllByProductIdIn(List.of(testProduct.getId())))
                .thenReturn(Collections.singletonList(testFeedback));

        List<ProductWithFeedbackDTO> productWithFeedbackDTOs = productService.getAllProductsWithFeedbacks(testPaginationAndSortParams);

        assertEquals(1, productWithFeedbackDTOs.size());
        assertEquals(testProduct.getId(), productWithFeedbackDTOs.get(0).getId());
        assertEquals(testProduct.getName(), productWithFeedbackDTOs.get(0).getName());
        assertEquals(1, productWithFeedbackDTOs.get(0).getFeedbacks().size());
        assertEquals(testFeedback.getId(), productWithFeedbackDTOs.get(0).getFeedbacks().get(0).getId());
        assertEquals(testFeedback.getProductId(), productWithFeedbackDTOs.get(0).getFeedbacks().get(0).getProductId());

        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
        verify(productRepository, times(1)).findAllProducts(pageable);
        verify(feedbackRepository, times(1)).findAllByProductIdIn(List.of(testProduct.getId()));
    }

    @Test
    void shouldSaveProduct() {
        when(productRepository.save(testProduct)).thenReturn(testProduct);

        Product savedProduct = productService.saveProduct(testProduct);

        assertEquals(testProduct, savedProduct);

        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void ShouldSaveProductWithImage() {
        when(imageUploader.uploadImage(file)).thenReturn("testUrl");
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductDTO result = productService.saveProductWithImage(testProductRequestDTO, file, authentication);

        assertEquals(testProduct.getName(), result.getName());
        assertEquals(testProduct.getDescription(), result.getDescription());
        assertEquals(testProduct.getImageUrl(), result.getImageUrl());

        verify(userService, times(1)).verifyUserHasAdminRole(authentication);
        verify(fileValidator, times(1)).validateFileIsNotEmpty(file);
        verify(imageUploader, times(1)).uploadImage(file);
        verify(productRepository, times(1)).save(testProduct);
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
}
