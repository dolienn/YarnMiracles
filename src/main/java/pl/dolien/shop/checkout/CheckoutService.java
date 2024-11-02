package pl.dolien.shop.checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dolien.shop.customer.CustomerService;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.dashboard.DashboardService;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.order.OrderService;
import pl.dolien.shop.order.OrderSetupService;
import pl.dolien.shop.purchase.Purchase;
import pl.dolien.shop.purchase.PurchaseResponse;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final DashboardService dashboardService;
    private final OrderSetupService orderSetupService;

    @Transactional
    public PurchaseResponse placeOrder(Purchase purchase, Authentication auth) {
        Order order = orderSetupService.initializeOrder(purchase);
        Customer customer = customerService.processCustomer(order, purchase.getCustomer(), auth);
        orderService.addOrderItems(order, purchase.getOrderItems());
        customerService.updatePurchasedProducts(customer, purchase.getOrderItems());
        dashboardService.updateOrderMetrics(order);

        return new PurchaseResponse(order.getOrderTrackingNumber());
    }
}
