package pl.dolien.shop.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts(@ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getAllProducts(paginationAndSortParams);
    }

    @GetMapping("/feedbacks")
    public List<ProductWithFeedbackDTO> getAllProductsWithFeedbacks(@ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getAllProductsWithFeedbacks(paginationAndSortParams);
    }

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsByCategoryId(@PathVariable Long id,
                                                 @ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getProductsByCategoryId(id, paginationAndSortParams);
    }

    @GetMapping("/search")
    public List<ProductDTO> getProductsByName(@RequestParam String name,
                                           @ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getProductsByNameContaining(name, paginationAndSortParams);
    }

    @PostMapping
    public ProductDTO addProduct(
            @RequestPart("product") @Valid ProductRequestDTO request,
            @RequestPart("file") @Valid @NotNull(message = "File is mandatory") MultipartFile file,
            Authentication connectedUser
    ) {
        return productService.saveProductWithImage(request, file, connectedUser);
    }
}
