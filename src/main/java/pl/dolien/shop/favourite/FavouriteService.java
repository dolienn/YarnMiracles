package pl.dolien.shop.favourite;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductAlreadyFavouriteException;
import pl.dolien.shop.exception.ProductNotFavouriteException;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.product.dto.ProductDTO;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.List;

import static pl.dolien.shop.product.ProductMapper.toProductDTOs;

@Service
@RequiredArgsConstructor
public class FavouriteService {

    private final ProductService productService;
    private final UserService userService;
    private final PageableBuilder pageableBuilder;
    private final FavouriteRepository favouriteRepository;

    @Cacheable(cacheNames = "favouritesByUser", keyGenerator = "customKeyGenerator")
    public Page<ProductDTO> getFavourites(Integer userId,
                                          PaginationAndSortParams paginationAndSortParams,
                                          Authentication connectedUser) {
        userService.verifyUserIsAuthenticatedUser(userId, connectedUser);

        Pageable pageable = pageableBuilder.buildPageable(paginationAndSortParams);

        return toProductDTOs(favouriteRepository.findFavouritesByUserId(userId, pageable));
    }

    @CacheEvict(cacheNames = "favouritesByUser", allEntries = true)
    public void addToFavourites(Integer userId,
                                Long productId,
                                Authentication connectedUser) {
        userService.verifyUserIsAuthenticatedUser(userId, connectedUser);

        UserProductTuple<User, Product> userProduct = getUserAndProduct(userId, productId);
        modifyFavouriteProduct(userProduct, true);
    }

    @CacheEvict(cacheNames = "favouritesByUser", allEntries = true)
    public void removeFromFavourites(Integer userId,
                                     Long productId,
                                     Authentication connectedUser) {
        userService.verifyUserIsAuthenticatedUser(userId, connectedUser);

        UserProductTuple<User, Product> userProduct = getUserAndProduct(userId, productId);
        modifyFavouriteProduct(userProduct, false);
    }

    private UserProductTuple<User, Product> getUserAndProduct(Integer userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        return new UserProductTuple<>(user, product);
    }

    private void modifyFavouriteProduct(UserProductTuple<User, Product> userProduct, boolean isAdding) {
        User user = userProduct.first();
        Product product = userProduct.second();
        if (isAdding) {
            validateProductNotFavourite(user, product);
            addProductToFavourites(user, product);
        } else {
            validateProductIsFavourite(user, product);
            removeProductFromFavourites(user, product);
        }
        userService.saveUser(user);
    }

    private void validateProductNotFavourite(User user, Product product) {
        if (user.getFavourites().contains(product)) {
            throw new ProductAlreadyFavouriteException("Product is already a favourite of the user");
        }
    }

    private void addProductToFavourites(User user, Product product) {
        user.getFavourites().add(product);
        product.getFavouritedBy().add(user);
    }

    private void validateProductIsFavourite(User user, Product product) {
        if (!user.getFavourites().contains(product)) {
            throw new ProductNotFavouriteException("Product is not a favourite of the user");
        }
    }

    private void removeProductFromFavourites(User user, Product product) {
        user.getFavourites().remove(product);
        product.getFavouritedBy().remove(user);
    }
}
