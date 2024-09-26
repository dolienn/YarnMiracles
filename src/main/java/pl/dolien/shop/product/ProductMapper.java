package pl.dolien.shop.product;

import org.springframework.stereotype.Service;
import pl.dolien.shop.feedback.Feedback;
import pl.dolien.shop.feedback.FeedbackRequest;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request, String imageUrl) {
        if(request == null) {
            throw new NullPointerException("The feedback request should not be null");
        }
        return Product.builder()
                .category(request.getCategory())
                .name(request.getName())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .unitsInStock(request.getUnitsInStock())
                .active(true)
                .imageUrl(imageUrl)
                .sales(0L)
                .build();
    }
}
