package pl.dolien.shop.product.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.dolien.shop.productCategory.ProductCategory;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProductRequestDTO {
    @NotNull(message = "Category field cannot be empty")
    private ProductCategory category;

    @NotEmpty(message = "Name field cannot be empty")
    @NotBlank(message = "Name field cannot be empty")
    private String name;

    @NotEmpty(message = "Description field cannot be empty")
    @NotBlank(message = "Description field cannot be empty")
    private String description;

    @NotNull(message = "Enter a valid price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal unitPrice;

    @NotNull(message = "Enter a valid units in stock")
    private int unitsInStock;
}
