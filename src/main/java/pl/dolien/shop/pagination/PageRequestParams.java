package pl.dolien.shop.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestParams {
    private String sortOrderType;
    private Integer page = 0;
    private Integer size = 20;
}
