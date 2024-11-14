package pl.dolien.shop.sort;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import pl.dolien.shop.exception.InvalidSortOrderException;

import static org.junit.jupiter.api.Assertions.*;

class SortGeneratorTest {

    private static final String INVALID_SORT_ORDER_TYPE = "INVALID_SORT_ORDER_TYPE";
    private static final String INVALID_SORT_ORDER_TYPE_MESSAGE = "Invalid sort order type provided: " + INVALID_SORT_ORDER_TYPE;

    private SortGenerator sortGenerator;

    @BeforeEach
    public void setUp() {
        sortGenerator = new SortGenerator();
    }

    @Test
    public void shouldGenerateSortWithNullSortOrderType() {
        Sort result = sortGenerator.generateSort(null);

        assertTrue(result.isUnsorted());
    }

    @Test
    public void shouldGenerateSortWithValidSortOrderType() {
        Sort result = sortGenerator.generateSort("PRICE_ASC");

        assertNotNull(result);
        assertTrue(result.isSorted());
    }

    @Test
    public void shouldGenerateSortWithInvalidSortOrderType() {
        InvalidSortOrderException exception = assertThrows(
                InvalidSortOrderException.class,
                () -> sortGenerator.generateSort(INVALID_SORT_ORDER_TYPE)
        );

        assertEquals(INVALID_SORT_ORDER_TYPE_MESSAGE, exception.getMessage());
    }
}
