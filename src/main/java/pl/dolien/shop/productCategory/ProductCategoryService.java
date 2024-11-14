package pl.dolien.shop.productCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductCategoryNotFoundException;

import static pl.dolien.shop.productCategory.ProductCategoryMapper.toProductCategoryDTO;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Cacheable(cacheNames = "category", key = "#categoryName")
    public ProductCategoryDTO getByCategoryName(String categoryName) {
        return toProductCategoryDTO(productCategoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found")));
    }
}
