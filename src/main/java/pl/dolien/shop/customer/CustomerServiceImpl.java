package pl.dolien.shop.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductService;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserRepository;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Transactional
    @Override
    public Customer processCustomer(Order order, Customer customer, Authentication connectedUser) {
        Customer existingCustomer = getCustomerByEmail(customer.getEmail());

        if (existingCustomer == null) {
            customerRepository.save(customer);
            customer.add(order);
            return customer;
        }

        if (isAuthenticatedCustomer(connectedUser, customer))
            updateCustomerInfo(existingCustomer, customer);

        existingCustomer.add(order);
        return existingCustomer;
    }

    @Transactional
    @Override
    public void updatePurchasedProducts(Customer customer, Set<OrderItem> orderItems) {
        User user = userRepository.findByEmail(customer.getEmail()).orElse(null);

        if(user != null) {
            orderItems.forEach(orderItem -> associateProductWithUser(orderItem, user));
        }
    }

    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }

    private void associateProductWithUser(OrderItem orderItem, User user) {
        Product product = productService.getProductById(orderItem.getProductId());
        product.addBuyer(user);
        user.addToPurchasedProducts(product);
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

