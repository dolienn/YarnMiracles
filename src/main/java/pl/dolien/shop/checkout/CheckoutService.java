package pl.dolien.shop.checkout;

import lombok.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.order.Customer;
import pl.dolien.shop.order.CustomerRepository;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.purchase.Purchase;
import pl.dolien.shop.purchase.PurchaseResponse;
import pl.dolien.shop.user.User;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CustomerRepository customerRepository;

    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase, Authentication auth) {
        Order order = purchase.getOrder();

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);

        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        Customer customer = purchase.getCustomer();

        order.setStatus("pending");

        Customer customerFromDB = customerRepository.findByEmail(customer.getEmail());

        if(customerFromDB != null) {
            if (auth != null && Objects.equals(((User) auth.getPrincipal()).getEmail(), customer.getEmail())) {
                customerFromDB.setFirstname(customer.getFirstname());
                customerFromDB.setLastname(customer.getLastname());
                customerFromDB.setOrders(customer.getOrders());
                customerFromDB.add(order);
                customer = customerFromDB;
            } else {
                customer = customerFromDB;
                customer.add(order);
            }
        } else {
            customer.add(order);
        }



        customerRepository.save(customer);

        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
