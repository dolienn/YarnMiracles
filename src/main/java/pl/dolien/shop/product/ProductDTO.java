package pl.dolien.shop.product;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductDTO {
    @NotNull(message = "Enter a valid category")
    private ProductCategory category;

    @NotEmpty(message = "Enter a valid name")
    @NotBlank(message = "Enter a valid name")
    private String name;

    @NotEmpty(message = "Enter a valid description")
    @NotBlank(message = "Enter a valid description")
    private String description;

    @NotNull(message = "Enter a valid price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal unitPrice;

    @NotNull(message = "Enter a valid units in stock")
    private int unitsInStock;
}
