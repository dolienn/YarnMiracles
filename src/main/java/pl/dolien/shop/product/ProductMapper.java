package pl.dolien.shop.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.pagination.RestPage;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.dolien.shop.feedback.FeedbackMapper.toFeedbackDTOs;

public class ProductMapper {

    private ProductMapper() {}

    public static Product toProduct(ProductRequestDTO request, String imageUrl) {
        return Product.builder()
                .categoryId(request.getCategory().getId())
                .name(request.getName())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .active(true)
                .imageUrl(imageUrl)
                .sales(0L)
                .build();
    }

    public static Page<ProductDTO> toProductDTOs(Page<Product> products) {
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());

        Pageable pageable = products.getPageable();

        return new RestPage<>(productDTOs, pageable.getPageNumber(), pageable.getPageSize(), products.getTotalElements());
    }

    public static ProductDTO toProductDTO(Product post) {
        return ProductDTO.builder()
                .id(post.getId())
                .categoryId(post.getCategoryId())
                .sku(post.getSku())
                .name(post.getName())
                .description(post.getDescription())
                .unitPrice(post.getUnitPrice())
                .imageUrl(post.getImageUrl())
                .active(post.isActive())
                .unitsInStock(post.getUnitsInStock())
                .rate(post.getRate())
                .sales(post.getSales())
                .dateCreated(post.getDateCreated())
                .build();
    }

    public static Page<ProductWithFeedbackDTO> toProductWithFeedbackDTOs(Page<ProductDTO> productDTOs, List<Feedback> feedbacks) {
        List<ProductWithFeedbackDTO> productWithFeedbackDTOs = productDTOs.stream()
                .map(productDTO -> toProductWithFeedbackDTO(productDTO, feedbacks))
                .collect(Collectors.toList());

        Pageable pageable = productDTOs.getPageable();

        return new RestPage<>(productWithFeedbackDTOs, pageable.getPageNumber(), pageable.getPageSize(), productDTOs.getTotalElements());
    }

    public static ProductWithFeedbackDTO toProductWithFeedbackDTO(ProductDTO productDTO,
                                                                  List<Feedback> feedbacks) {
        List<FeedbackDTO> productFeedbacks = extractFeedbacks(feedbacks, productDTO.getId());

        return ProductWithFeedbackDTO.builder()
                .id(productDTO.getId())
                .categoryId(productDTO.getCategoryId())
                .sku(productDTO.getSku())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .unitPrice(productDTO.getUnitPrice())
                .imageUrl(productDTO.getImageUrl())
                .active(productDTO.isActive())
                .unitsInStock(productDTO.getUnitsInStock())
                .rate(productDTO.getRate())
                .sales(productDTO.getSales())
                .feedbacks(productFeedbacks)
                .build();
    }

    private static List<FeedbackDTO> extractFeedbacks(List<Feedback> feedbacks, Long productId) {
        List<Feedback> productFeedbacks = feedbacks.stream()
                .filter(feedback -> Objects.equals(feedback.getProductId(), productId))
                .collect(Collectors.toList());
        return toFeedbackDTOs(productFeedbacks);
    }
}
