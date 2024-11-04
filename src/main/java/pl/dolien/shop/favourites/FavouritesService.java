package pl.dolien.shop.favourites;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dolien.shop.exception.ProductAlreadyFavouriteException;
import pl.dolien.shop.exception.ProductNotFavouriteException;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.pagination.PageableBuilder;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

@Service
@RequiredArgsConstructor
public class FavouritesService {

    private final ProductService productService;
    private final UserService userService;
    private final PageableBuilder pageableBuilder;
    private final FavouritesRepository favouritesRepository;

    public Page<Product> getFavourites(Integer userId, PageRequestParams pageRequestParams) {
        Pageable pageable = pageableBuilder.buildPageable(pageRequestParams);

        return favouritesRepository.findFavouritesByUserId(userId, pageable);
    }

    public void addFavouriteProduct(Integer userId, Long productId) {
        UserProductTuple<User, Product> userProduct = getUserAndProduct(userId, productId);
        modifyFavouriteProduct(userProduct, true);
    }

    public void removeFavouriteProduct(Integer userId, Long productId) {
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
        product.getFavouritedByUsers().add(user);
    }

    private void validateProductIsFavourite(User user, Product product) {
        if (!user.getFavourites().contains(product)) {
            throw new ProductNotFavouriteException("Product is not a favourite of the user");
        }
    }

    private void removeProductFromFavourites(User user, Product product) {
        user.getFavourites().remove(product);
        product.getFavouritedByUsers().remove(user);
    }
}
