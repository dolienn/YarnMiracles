package pl.dolien.shop.productCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCategoryDTO {

    private Integer id;
    private String categoryName;
}
