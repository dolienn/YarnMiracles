package pl.dolien.shop.productCategory;

import pl.dolien.shop.productCategory.dto.ProductCategoryDTO;

import java.util.List;

public interface ProductCategoryService {

    ProductCategoryDTO getByCategoryName(String categoryName);

    List<ProductCategoryDTO> getAllProductCategories();
}
