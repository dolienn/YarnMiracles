package pl.dolien.shop.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductDTO productDTO, String imageUrl) {
        return Product.builder()
                .category(productDTO.getCategory())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .unitPrice(productDTO.getUnitPrice())
                .unitsInStock(productDTO.getUnitsInStock())
                .active(true)
                .imageUrl(imageUrl)
                .sales(0L)
                .build();
    }
}
