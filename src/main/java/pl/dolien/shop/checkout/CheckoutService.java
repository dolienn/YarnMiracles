package pl.dolien.shop.checkout;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.dashboard.DashboardDataRepository;
import pl.dolien.shop.order.Customer;
import pl.dolien.shop.order.CustomerRepository;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderItem;
import pl.dolien.shop.payment.PaymentInfo;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.purchase.Purchase;
import pl.dolien.shop.purchase.PurchaseResponse;
import pl.dolien.shop.user.User;
import pl.dolien.shop.user.UserRepository;

import java.util.*;

@Service
public class CheckoutService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final DashboardDataRepository dashboardDataRepository;

    CheckoutService(CustomerRepository customerRepository,
                    @Value("${stripe.key.secret}") String secretKey, ProductRepository productRepository,
                    UserRepository userRepository, DashboardDataRepository dashboardDataRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.dashboardDataRepository = dashboardDataRepository;
        Stripe.apiKey = secretKey;
    }

    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase, Authentication auth) {
        if(purchase == null) {
            throw new NullPointerException("Purchase not found");
        }

        Order order = purchase.getOrder();

        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(order::add);
        orderItems.forEach(orderItem -> {
            Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
            product.incrementSales(orderItem.getQuantity());
            if(product.getUnitsInStock() >= orderItem.getQuantity()) {
                product.removeUnitsInStock(orderItem.getQuantity());
            }

            dashboardDataRepository.findById(1L).ifPresent(dashboardData -> {
                dashboardData.setProductsSell(dashboardData.getProductsSell() + orderItem.getQuantity());
                dashboardDataRepository.save(dashboardData);
            });

            productRepository.save(product);
        });

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

        Optional<User> user = userRepository.findByEmail(customer.getEmail());
        if(user.isPresent()) {
            User userFromDB = user.get();
            orderItems.forEach(orderItem -> {
                Optional<Product> productFromDB = productRepository.findById(orderItem.getProductId());
                productFromDB.ifPresent(userFromDB::addToPurchasedProducts);
                productFromDB.ifPresent(product -> product.addUserWhoPurchased(userFromDB));
                productRepository.save(productFromDB.orElseThrow(() -> new IllegalArgumentException("Product not found")));
            });
            userRepository.save(userFromDB);
        }

        customerRepository.save(customer);

        dashboardDataRepository.findById(1L).ifPresent(dashboardData -> {
            dashboardData.setTotalOrders(dashboardData.getTotalOrders() + 1);
            dashboardData.setThisMonthRevenue(dashboardData.getThisMonthRevenue().add(order.getTotalPrice()));
            dashboardDataRepository.save(dashboardData);
        });

        return new PurchaseResponse(orderTrackingNumber);
    }

    public PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfo.getAmount());
        params.put("currency", paymentInfo.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);
        params.put("description", "E-commerce shop purchase");
        params.put("receipt_email", paymentInfo.getReceiptEmail());

        return PaymentIntent.create(params);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }
}
