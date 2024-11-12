package pl.dolien.shop.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserService;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UserService userService;
    private final ProductService productService;
    private final KafkaTemplate<String, OrderItem> kafkaTemplate;

    @Transactional
    public Customer processCustomer(Order order, Customer customer, Authentication connectedUser) {
        Customer existingCustomer = getCustomerByEmail(customer.getEmail());

        if (existingCustomer == null) {
            customer.add(order);
            return customer;
        }

        if (isAuthenticatedCustomer(connectedUser, customer)) {
            updateCustomerInfo(existingCustomer, customer);
        }

        existingCustomer.add(order);

        return existingCustomer;
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    public void updatePurchasedProducts(Customer customer, Set<OrderItem> orderItems) {
        User user = userService.getUserByEmail(customer.getEmail());

        orderItems.forEach(orderItem -> associateProductWithUser(orderItem, user));

        userService.saveUser(user);
    }

    private void associateProductWithUser(OrderItem orderItem, User user) {
        Product product = productService.getProductById(orderItem.getProductId());
        user.addToPurchasedProducts(product);
        product.addUserWhoPurchased(user);
        productService.saveProduct(product);
    }

    private boolean isAuthenticatedCustomer(Authentication auth, Customer customer) {
        return auth != null && Objects.equals(((User) auth.getPrincipal()).getEmail(), customer.getEmail());
    }

    private void updateCustomerInfo(Customer existingCustomer, Customer newCustomer) {
        existingCustomer.setFirstname(newCustomer.getFirstname());
        existingCustomer.setLastname(newCustomer.getLastname());
        existingCustomer.setOrders(newCustomer.getOrders());
    }
}

