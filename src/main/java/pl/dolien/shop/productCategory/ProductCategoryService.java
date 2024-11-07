package pl.dolien.shop.productCategory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductCategoryNotFoundException;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategory getByCategoryName(String categoryName) {
        return productCategoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found"));
    }

    public ProductCategory getById(Integer id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new ProductCategoryNotFoundException("Product category not found"));
    }
}
