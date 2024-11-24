package pl.dolien.shop.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import static pl.dolien.shop.product.ProductMapper.toProductDTO;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductDTO getProductById(@PathVariable Long productId) {
        return toProductDTO(productService.getProductById(productId));
    }

    @GetMapping
    public Page<ProductDTO> getAllProducts(@ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getAllProducts(paginationAndSortParams);
    }

    @GetMapping("/category/{categoryId}")
    public Page<ProductDTO> getProductsByCategoryId(@PathVariable Integer categoryId,
                                                 @ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getProductsByCategoryId(categoryId, paginationAndSortParams);
    }

    @GetMapping("/search/{keyword}")
    public Page<ProductDTO> getProductsByKeyword(@PathVariable String keyword,
                                           @ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getProductsByKeyword(keyword, paginationAndSortParams);
    }

    @GetMapping("/feedbacks")
    public Page<ProductWithFeedbackDTO> getAllProductsWithFeedbacks(@ModelAttribute PaginationAndSortParams paginationAndSortParams) {
        return productService.getAllProductsWithFeedbacks(paginationAndSortParams);
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
