package pl.dolien.shop.favourite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.exception.ProductAlreadyFavouriteException;
import pl.dolien.shop.exception.ProductNotFavouriteException;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavouriteServiceTest {

    private static final String PRODUCT_ALREADY_FAVOURITE_MESSAGE = "Product is already a favourite of the user";
    private static final String PRODUCT_NOT_FAVOURITE_MESSAGE = "Product is not a favourite of the user";

    @InjectMocks
    private FavouriteService favouriteService;

    @Mock
    private FavouriteRepository favouriteRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private Authentication authentication;

    @Mock
    private PageableBuilder pageableBuilder;

    @Mock
    private Pageable pageable;

    private Product testProduct;
    private User testUser;
    private PaginationAndSortParams testPaginationAndSortParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldGetFavouritesByUserId() {
        when(pageableBuilder.buildPageable(testPaginationAndSortParams)).thenReturn(pageable);
        when(favouriteRepository.findFavouritesByUserId(testUser.getId(), pageable)).thenReturn(List.of(testProduct));

        List<ProductDTO> products = favouriteService.getFavourites(
                testUser.getId(),
                testPaginationAndSortParams,
                authentication
        );

        assertEquals(1, products.size());
        assertEquals(testProduct.getId(), products.get(0).getId());
        assertEquals(testProduct.getName(), products.get(0).getName());

        verify(pageableBuilder, times(1)).buildPageable(testPaginationAndSortParams);
        verify(favouriteRepository, times(1)).findFavouritesByUserId(1, pageable);
    }

    @Test
    void shouldAddProductToFavourites() {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getId())).thenReturn(testProduct);

        favouriteService.addToFavourites(testUser.getId(), testProduct.getId(), authentication);

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(productService, times(1)).getProductById(testProduct.getId());
    }

    @Test
    void shouldThrowExceptionWhenProductIsAlreadyFavourite() {
        testUser.getFavourites().add(testProduct);

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getId())).thenReturn(testProduct);

        ProductAlreadyFavouriteException exception = assertThrows(ProductAlreadyFavouriteException.class,
                () -> favouriteService.addToFavourites(testUser.getId(), testProduct.getId(), authentication));

        assertEquals(PRODUCT_ALREADY_FAVOURITE_MESSAGE, exception.getMessage());

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(productService, times(1)).getProductById(testProduct.getId());
    }

    @Test
    void shouldRemoveProductFromFavourites() {
        testUser.getFavourites().add(testProduct);

        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getId())).thenReturn(testProduct);

        favouriteService.removeFromFavourites(testUser.getId(), testProduct.getId(), authentication);

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(productService, times(1)).getProductById(testProduct.getId());
    }

    @Test
    void shouldThrowExceptionWhenProductIsNotFavourite() {
        when(userService.getUserById(testUser.getId())).thenReturn(testUser);
        when(productService.getProductById(testProduct.getId())).thenReturn(testProduct);

        ProductNotFavouriteException exception = assertThrows(ProductNotFavouriteException.class,
                () -> favouriteService.removeFromFavourites(testUser.getId(), testProduct.getId(), authentication));

        assertEquals(PRODUCT_NOT_FAVOURITE_MESSAGE, exception.getMessage());

        verify(userService, times(1)).getUserById(testUser.getId());
        verify(productService, times(1)).getProductById(testProduct.getId());
    }

    private void initializeTestData() {
        testProduct = Product.builder()
                .id(1L)
                .name("Test product")
                .favouritedBy(new HashSet<>())
                .build();

        testUser = User.builder()
                .id(1)
                .email("test@example.com")
                .favourites(new HashSet<>())
                .build();

        testPaginationAndSortParams = PaginationAndSortParams.builder()
                .page(0)
                .size(10)
                .sortOrderType("PRICE_ASC")
                .build();
    }
}