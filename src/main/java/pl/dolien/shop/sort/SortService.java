package pl.dolien.shop.sort;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.InvalidSortOrderException;

@Service
public class SortService {
    public Sort generateSort(String sortOrderType) {
        if (isSortOrderTypeNull(sortOrderType)) {
            return Sort.unsorted();
        }

        SortOrder validatedSortOrder  = parseAndValidateSortOrder(sortOrderType);
        return createSort(validatedSortOrder);
    }

    private boolean isSortOrderTypeNull(String sortOrderType) {
        return sortOrderType == null;
    }

    private SortOrder parseAndValidateSortOrder(String sortOrderType) {
        SortOrder sortOrder;
        try {
            sortOrder = SortOrder.valueOf(sortOrderType);
        } catch (IllegalArgumentException e) {
            throw new InvalidSortOrderException("Invalid sort order type provided: " + sortOrderType);
        }

        return sortOrder;
    }

    private Sort createSort(SortOrder sortOrder) {
        return Sort.by(Sort.Direction.fromString(sortOrder.getDirection()), sortOrder.getProperty());
    }
}
