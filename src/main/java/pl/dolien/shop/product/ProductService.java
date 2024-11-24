package pl.dolien.shop.product;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;

public interface ProductService {

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    Page<ProductDTO> getAllProducts(PaginationAndSortParams paginationAndSortParams);

    Page<ProductDTO> getProductsByCategoryId(Integer categoryId, PaginationAndSortParams paginationAndSortParams);

    Page<ProductDTO> getProductsByKeyword(String keyword, PaginationAndSortParams paginationAndSortParams);

    Page<ProductWithFeedbackDTO> getAllProductsWithFeedbacks(PaginationAndSortParams paginationAndSortParams);

    Product saveProduct(Product product);

    ProductDTO saveProductWithImage(ProductRequestDTO request, MultipartFile file, Authentication connectedUser);
}

