//package pl.dolien.shop.user;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import pl.dolien.shop.favourites.FavouritesService;
//import pl.dolien.shop.product.Product;
//import pl.dolien.shop.product.ProductRepository;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @InjectMocks
//    private UserService service;
//
//    @InjectMocks
//    private FavouritesService favouritesService;
//
//    @Mock
//    private UserRepository repository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    @Test
//    public void shouldAddFavouriteProduct() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(new ArrayList<>())
//                .build();
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .usersWhoFavourited(new ArrayList<>())
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        favouritesService.addFavouriteProduct(userId, productId);
//
//
//        verify(repository, times(1)).findById(userId);
//        verify(productRepository, times(1)).findById(productId);
//        assert user != null;
//        verify(repository, times(1)).save(user);
//
//        assertEquals(1, user.getFavourites().size());
//        assertTrue(user.getFavourites().contains(product));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserIdIsZero() {
//        Integer userId = 0;
//        Long productId = 2L;
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(new ArrayList<>())
//                .build();
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.addFavouriteProduct(userId, productId));
//        assertEquals("User id should not be zero", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenProductIdIsZero() {
//        Integer userId = 1;
//        Long productId = 0L;
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(new ArrayList<>())
//                .build();
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.addFavouriteProduct(userId, productId));
//        assertEquals("Product id should not be zero", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserNotFound() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.empty());
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.addFavouriteProduct(userId, productId));
//        assertEquals("User not found", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenProductNotFound() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(new ArrayList<>())
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.addFavouriteProduct(userId, productId));
//        assertEquals("Product not found", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserAlreadyFavouriteTheProduct() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(Collections.singletonList(product))
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.addFavouriteProduct(userId, productId));
//        assertEquals("Product is a favourite of the user", exp.getMessage());
//    }
//
//    @Test
//    public void shouldRemoveFavouriteProduct() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .build();
//
//        product.setUsersWhoFavourited(new ArrayList<>());
//        product.getUsersWhoFavourited().add(user);
//        user.setFavourites(new ArrayList<>());
//        user.getFavourites().add(product);
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//
//        favouritesService.removeFavouriteProduct(userId, productId);
//
//        verify(repository, times(1)).findById(userId);
//        verify(productRepository, times(1)).findById(productId);
//        verify(repository, times(1)).save(user);
//
//        assertEquals(0, user.getFavourites().size());
//        assertFalse(user.getFavourites().contains(product));
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserIdIsZeroUsingRemove() {
//        Integer userId = 0;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .build();
//
//        user.setFavourites(new ArrayList<>());
//        user.getFavourites().add(product);
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.removeFavouriteProduct(userId, productId));
//        assertEquals("User id should not be zero", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenProductIdIsZeroUsingRemove() {
//        Integer userId = 1;
//        Long productId = 0L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .build();
//
//        user.setFavourites(new ArrayList<>());
//        user.getFavourites().add(product);
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.removeFavouriteProduct(userId, productId));
//        assertEquals("Product id should not be zero", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserNotFoundUsingRemove() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .build();
//
//        user.setFavourites(new ArrayList<>());
//        user.getFavourites().add(product);
//
//        when(repository.findById(userId)).thenReturn(Optional.empty());
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.removeFavouriteProduct(userId, productId));
//        assertEquals("User not found", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenProductNotFoundUsingRemove() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .build();
//
//        user.setFavourites(new ArrayList<>());
//        user.getFavourites().add(product);
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.empty());
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.removeFavouriteProduct(userId, productId));
//        assertEquals("Product not found", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenUserNotFavouriteTheProduct() {
//        Integer userId = 1;
//        Long productId = 2L;
//
//        Product product = Product.builder()
//                .id(productId)
//                .name("Ball")
//                .build();
//
//        User user = User.builder()
//                .id(userId)
//                .email("test@test.com")
//                .password("test123")
//                .favourites(new ArrayList<>())
//                .build();
//
//        when(repository.findById(userId)).thenReturn(Optional.ofNullable(user));
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> favouritesService.removeFavouriteProduct(userId, productId));
//        assertEquals("Product is not a favourite of the user", exp.getMessage());
//    }
//}