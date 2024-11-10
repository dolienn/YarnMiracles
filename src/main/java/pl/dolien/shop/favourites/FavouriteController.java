package pl.dolien.shop.favourites;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Tag(name = "Favourite")
public class FavouriteController {

    private final FavouriteService favouriteService;

    @GetMapping("/{userId}/favourites")
    public List<ProductDTO> getFavourites(@PathVariable Integer userId,
                                          @ModelAttribute PaginationAndSortParams paginationAndSortParams,
                                          Authentication connectedUser) {
        return favouriteService.getFavourites(userId, paginationAndSortParams, connectedUser);
    }

    @PostMapping("/{userId}/favourites/{productId}")
    public void addFavouriteProduct(@PathVariable Integer userId,
                                    @PathVariable Long productId,
                                    Authentication connectedUser) {
        favouriteService.addFavouriteProduct(userId, productId, connectedUser);
    }

    @DeleteMapping("/{userId}/favourites/{productId}")
    public void removeFavouriteProduct(@PathVariable Integer userId,
                                       @PathVariable Long productId,
                                       Authentication connectedUser) {
        favouriteService.removeFavouriteProduct(userId, productId, connectedUser);
    }
}
