package pl.dolien.shop.pagination;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.dolien.shop.sort.SortGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PageableBuilderTest {

    @InjectMocks
    private PageableBuilder pageableBuilder;

    @Mock
    private SortGenerator sortGenerator;

    private PaginationAndSortParams paginationAndSortParams;
    private PaginationParams paginationParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldBuildPageableWithPaginationAndSortParams() {
        when(sortGenerator.generateSort(paginationAndSortParams.getSortOrderType()))
                .thenReturn(Sort.by(Sort.Order.asc("p.unitPrice")));

        Pageable result = pageableBuilder.buildPageable(paginationAndSortParams);

        assertNotNull(result);
        assertEquals(paginationAndSortParams.getPage(), result.getPageNumber());
        assertEquals(paginationAndSortParams.getSize(), result.getPageSize());
        assertEquals(Sort.by(Sort.Order.asc("p.unitPrice")), result.getSort());

        verify(sortGenerator, times(1)).generateSort(paginationAndSortParams.getSortOrderType());
    }

    @Test
    void shouldBuildPageableWithPaginationParams() {
        Pageable result = pageableBuilder.buildPageable(paginationParams);

        assertNotNull(result);
        assertEquals(paginationParams.getPage(), result.getPageNumber());
        assertEquals(paginationParams.getSize(), result.getPageSize());
        assertTrue(result.getSort().isUnsorted());
    }

    private void initializeTestData() {
        paginationAndSortParams = PaginationAndSortParams.builder()
                .page(0)
                .size(10)
                .sortOrderType("PRICE_ASC")
                .build();

        paginationParams = PaginationParams.builder()
                .page(1)
                .size(12)
                .build();
    }
}
