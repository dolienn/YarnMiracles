package pl.dolien.shop.keyGenerator;

import org.springframework.stereotype.Component;
import pl.dolien.shop.pagination.PaginationAndSortParams;

@Component
public class PaginationAndSortParamsKeyFragmentGenerator implements KeyFragmentGenerator {

    @Override
    public void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        if (param instanceof PaginationAndSortParams paginationAndSortParams)
            appendPaginationAndSortParamsKeyFragment(keyBuilder, paginationAndSortParams);
    }

    private void appendPaginationAndSortParamsKeyFragment(StringBuilder keyBuilder, PaginationAndSortParams paginationAndSortParams) {
        keyBuilder.append("_page:").append(paginationAndSortParams.getPage())
                .append("_size:").append(paginationAndSortParams.getSize())
                .append("_sortOrderType:").append(paginationAndSortParams.getSortOrderType());
    }
}
