package pl.dolien.shop.keyGenerator;

import org.springframework.stereotype.Component;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.PaginationParams;

@Component
public class PageRequestParamsKeyFragmentGenerator implements KeyFragmentGenerator {

    @Override
    public void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        if (param instanceof PaginationAndSortParams paginationAndSortParams) {
            appendPaginationAndSortParamsKeyFragment(keyBuilder, paginationAndSortParams);
        } else if (param instanceof PaginationParams paginationParams) {
            appendPaginationParamsKeyFragment(keyBuilder, paginationParams);
        }
    }

    private void appendPaginationAndSortParamsKeyFragment(StringBuilder keyBuilder, PaginationAndSortParams paginationAndSortParams) {
        keyBuilder.append("_page:").append(paginationAndSortParams.getPage())
                .append("_size:").append(paginationAndSortParams.getSize())
                .append("_sortOrderType:").append(paginationAndSortParams.getSortOrderType());
    }

    private void appendPaginationParamsKeyFragment(StringBuilder keyBuilder, PaginationParams paginationParams) {
        keyBuilder.append("_page:").append(paginationParams.getPage())
                .append("_size:").append(paginationParams.getSize());
    }
}
