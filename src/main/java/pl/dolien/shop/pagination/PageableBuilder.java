package pl.dolien.shop.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.dolien.shop.sort.SortService;

@Service
@RequiredArgsConstructor
public class PageableBuilder {

    private final SortService sortService;

    public Pageable buildPageable(PageRequestParams pageRequestParams) {
        Sort sort = sortService.generateSort(pageRequestParams.getSortOrderType());
        return createValidatedPageRequest(pageRequestParams.getPage(), pageRequestParams.getSize(), sort);
    }

    private Pageable createValidatedPageRequest(Integer requestedPage, Integer requestedSize, Sort sort) {
        int validatedPage = (requestedPage != null && requestedPage >= 0) ? requestedPage : 0;
        int validatedSize = (requestedSize != null && requestedSize > 0) ? requestedSize : 20;
        return PageRequest.of(validatedPage, validatedSize, sort);
    }
}
