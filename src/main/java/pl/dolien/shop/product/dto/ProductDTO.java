package pl.dolien.shop.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private Integer categoryId;
    private String sku;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private String imageUrl;
    private boolean active;
    private int unitsInStock;
    private Double rate;
    private Long sales;
    private Date dateCreated;
}
