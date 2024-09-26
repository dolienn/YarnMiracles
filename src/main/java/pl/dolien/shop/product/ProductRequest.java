package pl.dolien.shop.product;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductRequest {
    @NotNull(message = "Category is mandatory")
    private ProductCategory category;

    @NotEmpty(message = "Name is mandatory")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotEmpty(message = "Description is mandatory")
    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal unitPrice;

    @NotNull(message = "Units in stock is mandatory")
    private int unitsInStock;
}
