package pl.dolien.shop.keyGenerator;

import org.springframework.stereotype.Component;
import pl.dolien.shop.pagination.PaginationParams;

@Component
public class PaginationParamsKeyFragmentGenerator implements KeyFragmentGenerator {

    @Override
    public void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        if (param instanceof PaginationParams paginationParams)
            appendPaginationParamsKeyFragment(keyBuilder, paginationParams);
    }

    private void appendPaginationParamsKeyFragment(StringBuilder keyBuilder, PaginationParams paginationParams) {
        keyBuilder.append("_page:").append(paginationParams.getPage())
                .append("_size:").append(paginationParams.getSize());
    }
}
