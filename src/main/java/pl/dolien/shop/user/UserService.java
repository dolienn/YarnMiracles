package pl.dolien.shop.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductRepository;

import java.util.List;

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

//    public List<Product> getFavouriteProducts(Integer userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        return user.getFavourites();
//    }

    public Page<Product> getFavouriteProducts(Integer userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> favourites = List.copyOf(user.getFavourites());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), favourites.size());

        return new PageImpl<>(favourites.subList(start, end), pageable, favourites.size());
    }
}