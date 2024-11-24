package pl.dolien.shop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private static final String PRODUCT_NAME = "Test product";

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductDTO testProductDTO;
    private ProductWithFeedbackDTO testProductWithFeedbackDTO;
    private PaginationAndSortParams testPaginationAndSortParams;
    private MockMultipartFile filePart;
    private MockMultipartFile productPart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        initializeTestData();
    }

    @Test
    void shouldGetAllProducts() throws Exception {
        when(productService.getAllProducts(any(PaginationAndSortParams.class))).thenReturn(
                buildRestPageByProductDTO(List.of(testProductDTO))
        );

        performGetAllProducts()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buildRestPageByProductDTO(List.of(testProductDTO)))));

        verify(productService, times(1)).getAllProducts(any(PaginationAndSortParams.class));
    }

    @Test
    void shouldGetProductsByCategoryId() throws Exception {
        when(productService.getProductsByCategoryId(anyInt(), any(PaginationAndSortParams.class)))
                .thenReturn(buildRestPageByProductDTO(List.of(testProductDTO)));

        performGetProductsByCategoryId()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buildRestPageByProductDTO(List.of(testProductDTO)))));

        verify(productService, times(1))
                .getProductsByCategoryId(anyInt(), any(PaginationAndSortParams.class));
    }

    @Test
    void shouldGetProductsByKeyword() throws Exception {
        when(productService.getProductsByKeyword(anyString(), any(PaginationAndSortParams.class)))
                .thenReturn(buildRestPageByProductDTO(List.of(testProductDTO)));

        performGetProductsByKeyword()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buildRestPageByProductDTO(List.of(testProductDTO)))));

        verify(productService, times(1))
                .getProductsByKeyword(anyString(), any(PaginationAndSortParams.class));
    }

    @Test
    void shouldGetAllProductsWithFeedbacks() throws Exception {
        when(productService.getAllProductsWithFeedbacks(any(PaginationAndSortParams.class)))
                .thenReturn(buildRestPageByProductWithFeedbackDTO(List.of(testProductWithFeedbackDTO)));

        performGetAllProductsWithFeedbacks()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(buildRestPageByProductWithFeedbackDTO(List.of(testProductWithFeedbackDTO)))));

        verify(productService, times(1)).getAllProductsWithFeedbacks(any(PaginationAndSortParams.class));
    }

    @Test
    void shouldAddProductWithImage() throws Exception {
        when(productService.saveProductWithImage(
                any(ProductRequestDTO.class), any(MockMultipartFile.class), any(Authentication.class)
        ))
                .thenReturn(testProductDTO);

        performAddProductWithImage()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testProductDTO)));

        verify(productService, times(1))
                .saveProductWithImage(
                        any(ProductRequestDTO.class), any(MockMultipartFile.class), any(Authentication.class)
                );
    }

    @Test
    void shouldThrowExceptionWhenAddingProductWithoutImage() throws Exception {
        performAddProductWithoutImage()
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .saveProductWithImage(
                        any(ProductRequestDTO.class), any(MockMultipartFile.class), any(Authentication.class)
                );
    }

    @Test
    void shouldThrowExceptionWhenAddingProductWithoutProduct() throws Exception {
        performAddProductWithoutProduct()
                .andExpect(status().isBadRequest());

        verify(productService, never())
                .saveProductWithImage(
                        any(ProductRequestDTO.class), any(MockMultipartFile.class), any(Authentication.class)
                );
    }

    private void initializeTestData() {

        testProductDTO = ProductDTO.builder()
                .id(1L)
                .name(PRODUCT_NAME)
                .build();

        testProductWithFeedbackDTO = ProductWithFeedbackDTO.builder()
                .id(2L)
                .name(PRODUCT_NAME + " with feedback")
                .build();

        testPaginationAndSortParams = PaginationAndSortParams.builder()
                .page(1)
                .size(10)
                .sortBy("PRICE_ASC")
                .build();

        filePart = new MockMultipartFile("file", "image.jpg", MULTIPART_FORM_DATA_VALUE, "image content".getBytes());

        String productJson = """
                {
                  "category": {
                     "id": 1,
                     "categoryName": "Crochet Toys"
                  },
                  "name": "Test product",
                  "description": "Test description",
                  "unitPrice": 10.99
                }
                """;

        productPart = new MockMultipartFile("product", "", APPLICATION_JSON_VALUE, productJson.getBytes());
    }

    private ResultActions performGetAllProducts() throws Exception {
        return mockMvc.perform(get("/products")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams)));
    }

    private ResultActions performGetProductsByCategoryId() throws Exception {
        return mockMvc.perform(get("/products/category/{categoryId}", 1)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams)));
    }

    private ResultActions performGetProductsByKeyword() throws Exception {
        return mockMvc.perform(get("/products/search/{keyword}", "testName")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams)));
    }

    private ResultActions performGetAllProductsWithFeedbacks() throws Exception {
        return mockMvc.perform(get("/products/feedbacks")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams)));
    }

    private ResultActions performAddProductWithImage() throws Exception {
        return mockMvc.perform(multipart("/products")
                .file(filePart)
                .file(productPart)
                .principal(authentication));
    }

    private ResultActions performAddProductWithoutImage() throws Exception {
        return mockMvc.perform(multipart("/products")
                .file(productPart)
                .principal(authentication));
    }

    private ResultActions performAddProductWithoutProduct() throws Exception {
        return mockMvc.perform(multipart("/products")
                .file(filePart)
                .principal(authentication));
    }

    private Page<ProductDTO> buildRestPageByProductDTO(List<ProductDTO> content) {
        return new RestPage<>(content, 1, 1, 1);
    }

    private Page<ProductWithFeedbackDTO> buildRestPageByProductWithFeedbackDTO(List<ProductWithFeedbackDTO> content) {
        return new RestPage<>(content, 1, 1, 1);
    }
}
