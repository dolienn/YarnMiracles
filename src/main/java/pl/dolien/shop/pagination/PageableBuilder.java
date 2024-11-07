package pl.dolien.shop.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.dolien.shop.sort.SortGenerator;

@Service
@RequiredArgsConstructor
public class PageableBuilder {

    private static final int DEFAULT_PAGE_SIZE = 20;

    private final SortGenerator sortGenerator;

    public Pageable buildPageable(PageRequestParams pageRequestParams) {
        Sort sort = sortGenerator.generateSort(pageRequestParams.getSortOrderType());

        int validatedPage = validatePage(pageRequestParams.getPage());
        int validatedSize = validateSize(pageRequestParams.getSize());

        return PageRequest.of(validatedPage, validatedSize, sort);
    }

    private int validateSize(Integer size) {
        return (size != null && size > 0) ? size : DEFAULT_PAGE_SIZE;
    }

    private int validatePage(Integer page) {
        return (page != null && page >= 0) ? page : 0;
    }
}
