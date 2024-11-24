package pl.dolien.shop.productCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductCategoryNotFoundException;
import pl.dolien.shop.productCategory.dto.ProductCategoryDTO;

import java.util.List;

import static pl.dolien.shop.productCategory.ProductCategoryMapper.toProductCategoryDTO;
import static pl.dolien.shop.productCategory.ProductCategoryMapper.toProductCategoryDTOs;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Cacheable(cacheNames = "category", key = "#categoryName")
    public ProductCategoryDTO getByCategoryName(String categoryName) {
        return toProductCategoryDTO(productCategoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found")));
    }

    public List<ProductCategoryDTO> getAllProductCategories() {
        return toProductCategoryDTOs(productCategoryRepository.findAll());
    }
}
