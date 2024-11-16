package pl.dolien.shop.pagination;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationParams {
    private Integer page;
    private Integer size;
}
