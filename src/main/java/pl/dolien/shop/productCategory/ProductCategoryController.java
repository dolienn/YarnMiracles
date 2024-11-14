package pl.dolien.shop.productCategory;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product-category")
@RequiredArgsConstructor
@Tag(name = "Product-Category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/{categoryName}")
    public ProductCategoryDTO getByCategoryName(@PathVariable String categoryName) {
        return productCategoryService.getByCategoryName(categoryName);
    }
}
