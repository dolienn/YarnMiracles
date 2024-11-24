package pl.dolien.shop.productCategory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.dolien.shop.exception.ProductCategoryNotFoundException;
import pl.dolien.shop.productCategory.dto.ProductCategoryDTO;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCategoryServiceTest {

    private static final String CATEGORY_NAME = "Test category";

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    private ProductCategory productCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldGetProductCategoryDTOByCategoryName() {
        when(productCategoryRepository.findByCategoryName(CATEGORY_NAME)).thenReturn(Optional.of(productCategory));

        ProductCategoryDTO result = productCategoryService.getByCategoryName(CATEGORY_NAME);

        assertEquals(productCategory.getId(), result.getId());
        assertEquals(productCategory.getCategoryName(), result.getCategoryName());

        verify(productCategoryRepository, times(1)).findByCategoryName(CATEGORY_NAME);
    }

    @Test
    void shouldThrowProductCategoryNotFoundException() {
        when(productCategoryRepository.findByCategoryName(CATEGORY_NAME)).thenReturn(Optional.empty());

        assertThrows(ProductCategoryNotFoundException.class, () -> productCategoryService.getByCategoryName(CATEGORY_NAME));

        verify(productCategoryRepository, times(1)).findByCategoryName(CATEGORY_NAME);
    }

    private void initializeTestData() {
        productCategory = ProductCategory.builder()
                .id(1)
                .categoryName(CATEGORY_NAME)
                .build();
    }
}
