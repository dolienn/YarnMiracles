package pl.dolien.shop.productCategory.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCategoryDTO {

    private Integer id;
    private String categoryName;
}
