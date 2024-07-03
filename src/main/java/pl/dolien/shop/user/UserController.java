package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/favourites/{productId}")
    public void addFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.addFavouriteProduct(userId, productId);
    }

    @DeleteMapping("/{userId}/favourites/{productId}")
    public void removeFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.removeFavouriteProduct(userId, productId);
    }
}
