package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    public void addFavouriteProduct(Integer userId, Long productId) {
        if(userId == 0) {
            throw new IllegalArgumentException("User id should not be zero");
        }

        if(productId == 0) {
            throw new IllegalArgumentException("Product id should not be zero");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (user.getFavourites().contains(product)) {
            throw new IllegalArgumentException("Product is a favourite of the user");
        }

        user.getFavourites().add(product);
        product.getUsersWhoFavourited().add(user);
        userRepository.save(user);
    }

    public void removeFavouriteProduct(Integer userId, Long productId) {
        if(userId == 0) {
            throw new IllegalArgumentException("User id should not be zero");
        }

        if(productId == 0) {
            throw new IllegalArgumentException("Product id should not be zero");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!user.getFavourites().contains(product)) {
            throw new IllegalArgumentException("Product is not a favourite of the user");
        }

        user.getFavourites().remove(product);
        product.getUsersWhoFavourited().remove(user);
        userRepository.save(user);
    }
}
