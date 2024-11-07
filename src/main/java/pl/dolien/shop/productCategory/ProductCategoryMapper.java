package pl.dolien.shop.productCategory;

public class ProductCategoryMapper {

    private ProductCategoryMapper() {}

    public static ProductCategoryDTO toProductCategoryDTO(ProductCategory productCategory) {
        return ProductCategoryDTO.builder()
                .id(productCategory.getId())
                .categoryName(productCategory.getCategoryName())
                .build();
    }
}
