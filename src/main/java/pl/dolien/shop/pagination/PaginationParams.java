package pl.dolien.shop.pagination;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginationParams {
    private Integer page = 0;
    private Integer size = 20;
}
