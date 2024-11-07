package pl.dolien.shop.product;

import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.product.dto.ProductRequestDTO;
import pl.dolien.shop.product.dto.ProductWithFeedbackDTO;

import java.util.List;
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

    public static List<ProductDTO> toProductDTOs(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductDTO)
                .collect(Collectors.toList());
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
                .build();
    }

    public static List<ProductWithFeedbackDTO> toProductWithFeedbackDTOs(List<Product> products) {
        return products.stream()
                .map(ProductMapper::toProductWithFeedbackDTO)
                .collect(Collectors.toList());
    }

    public static ProductWithFeedbackDTO toProductWithFeedbackDTO(Product post) {
        return ProductWithFeedbackDTO.builder()
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
                .feedbacks(toFeedbackDTOs(post.getFeedbacks()))
                .build();
    }
}
