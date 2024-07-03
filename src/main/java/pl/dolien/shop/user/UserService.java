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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        user.getFavourites().add(product);
        userRepository.save(user);
    }

    public void removeFavouriteProduct(Integer userId, Long productId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        user.getFavourites().remove(product);
        userRepository.save(user);
    }
}
