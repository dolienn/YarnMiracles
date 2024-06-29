package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void addFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.addFavouriteProduct(userId, productId);
    }

    @DeleteMapping("/{userId}/favourites/{productId}")
    public void removeFavouriteProduct(@PathVariable Integer userId, @PathVariable Long productId) {
        userService.removeFavouriteProduct(userId, productId);
    }

//    @GetMapping("/{userId}/favourites")
//    public ResponseEntity<Page<Product>> getFavouriteProducts(@PathVariable Integer userId, Pageable pageable) {
//        Page<Product> favourites = userService.getFavouriteProducts(userId, pageable);
//        return ResponseEntity.ok(favourites);
//    }
}
