package pl.dolien.shop.sort;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortOrder {
    PRICE_ASC("p.unitPrice", "ASC"),
    PRICE_DESC("p.unitPrice", "DESC"),
    RATE_DESC("p.rate", "DESC"),
    SALES_DESC("p.sales", "DESC");

    private final String property;
    private final String direction;
}
