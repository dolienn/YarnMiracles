package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.security.JwtService;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/{userId}/favourites/{productId}")
    public ResponseEntity<String> addFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.addFavouriteProduct(userId, productId);
        return ResponseEntity.ok("Product added to favourites");
    }

    @DeleteMapping("/{userId}/favourites/{productId}")
    public ResponseEntity<String> removeFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.removeFavouriteProduct(userId, productId);
        return ResponseEntity.ok("Product removed from favourites");
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<List<Product>> getFavouriteProducts(@PathVariable Integer userId) {
        List<Product> favourites = userService.getFavouriteProducts(userId);
        return ResponseEntity.ok(favourites);
    }

}
