package pl.dolien.shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.pagination.PageRequestParams;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<Product> getAllProducts(@ModelAttribute PageRequestParams pageRequestParams) {
        return productService.getAllProducts(pageRequestParams);
    }

    @GetMapping("/category/{id}")
    public Page<Product> getProductsByCategoryId(@PathVariable Long id,
                                                 @ModelAttribute PageRequestParams pageRequestParams) {
        return productService.getProductsByCategoryId(id, pageRequestParams);
    }

    @GetMapping("/search")
    public Page<Product> getProductsByName(@RequestParam String name,
                                           @ModelAttribute PageRequestParams pageRequestParams) {
        return productService.getProductsByNameContaining(name, pageRequestParams);
    }
}
