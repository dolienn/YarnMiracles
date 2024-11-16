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

        Pageable response = pageableBuilder.buildPageable(paginationAndSortParams);

        assertPageableWithSort(response, paginationAndSortParams);

        verify(sortGenerator, times(1)).generateSort(paginationAndSortParams.getSortOrderType());
    }

    @Test
    void shouldBuildPageableWithPaginationParams() {
        Pageable response = pageableBuilder.buildPageable(paginationParams);

        assertPageableWithoutSort(response, paginationParams);
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

    private void assertPageableWithSort(Pageable pageable, PaginationAndSortParams params) {
        assertNotNull(pageable);
        assertEquals(params.getPage(), pageable.getPageNumber());
        assertEquals(params.getSize(), pageable.getPageSize());
        assertEquals(Sort.by(Sort.Order.asc("p.unitPrice")), pageable.getSort());
    }

    private void assertPageableWithoutSort(Pageable pageable, PaginationParams params) {
        assertNotNull(pageable);
        assertEquals(params.getPage(), pageable.getPageNumber());
        assertEquals(params.getSize(), pageable.getPageSize());
        assertTrue(pageable.getSort().isUnsorted());
    }
}
