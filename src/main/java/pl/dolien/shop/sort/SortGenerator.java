package pl.dolien.shop.sort;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.dolien.shop.exception.InvalidSortOrderException;

@Component
public class SortGenerator {
    public Sort generateSort(String sortBy) {
        if (sortBy == null)
            return Sort.unsorted();

        SortOrder validatedSortOrder  = parseAndValidateSortOrder(sortBy);
        return buildSort(validatedSortOrder);
    }

    private SortOrder parseAndValidateSortOrder(String sortBy) {
        SortOrder sortOrder;
        try {
            sortOrder = SortOrder.valueOf(sortBy);
        } catch (IllegalArgumentException e) {
            throw new InvalidSortOrderException("Invalid sort order type provided: " + sortBy);
        }

        return sortOrder;
    }

    private Sort buildSort(SortOrder sortOrder) {
        return Sort.by(Sort.Direction.fromString(sortOrder.getDirection()), sortOrder.getProperty());
    }
}
