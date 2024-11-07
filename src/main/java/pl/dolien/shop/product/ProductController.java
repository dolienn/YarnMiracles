package pl.dolien.shop.product;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;

import static pl.dolien.shop.product.ProductMapper.*;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
@Tag(name = "Product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getProducts(@ModelAttribute PageRequestParams pageRequestParams) {
        return toProductDTOs(productService.getAllProducts(pageRequestParams));
    }

    @GetMapping("/feedbacks")
    public List<ProductWithFeedbackDTO> getProductsWithFeedbacks(@ModelAttribute PageRequestParams pageRequestParams) {
        return toProductWithFeedbackDTOs(productService.getProductsWithFeedbacks(pageRequestParams));
    }

    @GetMapping("/category/{id}")
    public List<ProductDTO> getProductsByCategoryId(@PathVariable Long id,
                                                 @ModelAttribute PageRequestParams pageRequestParams) {
        return toProductDTOs(productService.getProductsByCategoryId(id, pageRequestParams));
    }

    @GetMapping("/search")
    public List<ProductDTO> getProductsByName(@RequestParam String name,
                                           @ModelAttribute PageRequestParams pageRequestParams) {
        return toProductDTOs(productService.getProductsByNameContaining(name, pageRequestParams));
    }

    @PostMapping
    public ProductDTO addProduct(
            @RequestPart("product") @Valid ProductRequestDTO request,
            @RequestPart("file") @Valid @NotNull(message = "File is mandatory") MultipartFile file,
            Authentication connectedUser
    ) {
        return toProductDTO(productService.saveProductWithImage(request, file, connectedUser));
    }
}
