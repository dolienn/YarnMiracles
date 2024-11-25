package pl.dolien.shop.productCategory;

import pl.dolien.shop.productCategory.dto.ProductCategoryDTO;

import java.util.List;

public class ProductCategoryMapper {

    private ProductCategoryMapper() {}

    public static List<ProductCategoryDTO> toProductCategoryDTOs(List<ProductCategory> productCategories) {
        return productCategories.stream()
                .map(ProductCategoryMapper::toProductCategoryDTO)
                .toList();
    }

    public static ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory) {
        return ProductCategoryDTO.builder()
                .id(productCategory.getId())
                .categoryName(productCategory.getCategoryName())
                .build();
    }
}
