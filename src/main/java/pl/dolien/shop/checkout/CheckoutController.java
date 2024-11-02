package pl.dolien.shop.checkout;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolien.shop.purchase.Purchase;
import pl.dolien.shop.purchase.PurchaseResponse;

@RestController
@RequestMapping("checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService service;

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody Purchase purchase, Authentication auth) {
        return service.placeOrder(purchase, auth);
    }
}
