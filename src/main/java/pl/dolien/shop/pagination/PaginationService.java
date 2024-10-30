package pl.dolien.shop.pagination;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {
    public Pageable createPageRequest(Integer page, Integer size, Sort sort) {
        int validatedPage = page != null && page >= 0 ? page : 0;
        int validatedSize = size != null && size > 0 ? size : 20;
        return PageRequest.of(validatedPage, validatedSize, sort);
    }
}
