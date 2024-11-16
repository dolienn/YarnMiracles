package pl.dolien.shop.productCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductCategoryControllerTest {

    private static final String CATEGORY_NAME = "Test category";

    @InjectMocks
    private ProductCategoryController productCategoryController;

    @Mock
    private ProductCategoryService productCategoryService;

    @Autowired
    private MockMvc mockMvc;

    private ProductCategoryDTO productCategoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productCategoryController).build();

        initializeTestData();
    }

    @Test
    void shouldGetProductCategoryDTOByCategoryName() throws Exception {
        when(productCategoryService.getByCategoryName(CATEGORY_NAME)).thenReturn(productCategoryDTO);

        mockMvc.perform(get("/product-category/{categoryName}", CATEGORY_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productCategoryDTO.getId()))
                .andExpect(jsonPath("$.categoryName").value(productCategoryDTO.getCategoryName()));

        verify(productCategoryService, times(1)).getByCategoryName(CATEGORY_NAME);
    }

    private void initializeTestData() {
        productCategoryDTO = ProductCategoryDTO.builder()
                .id(1)
                .categoryName(CATEGORY_NAME)
                .build();
    }
}
