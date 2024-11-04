package pl.dolien.shop.favourites;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.pagination.PageRequestParams;
import pl.dolien.shop.product.Product;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class FavouritesController {

    private final FavouritesService favouritesService;

    @GetMapping("/{userId}/favourites")
    public Page<Product> getFavourites(@PathVariable Integer userId,
                                       @ModelAttribute PageRequestParams pageRequestParams) {
        return favouritesService.getFavourites(userId, pageRequestParams);
    }

    @PostMapping("/{userId}/favourites/{productId}")
    public void addFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        favouritesService.addFavouriteProduct(userId, productId);
    }

    @DeleteMapping("/{userId}/favourites/{productId}")
    public void removeFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        favouritesService.removeFavouriteProduct(userId, productId);
    }
}
