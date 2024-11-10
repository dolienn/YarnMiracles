package pl.dolien.shop.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationAndSortParams {
    private Integer page = 0;
    private Integer size = 20;
    private String sortOrderType;
}
