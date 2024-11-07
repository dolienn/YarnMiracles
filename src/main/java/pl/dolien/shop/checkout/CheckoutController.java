package pl.dolien.shop.checkout;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("checkout")
@RequiredArgsConstructor
@Tag(name = "Checkout")
public class CheckoutController {

    private final CheckoutService service;

    @PostMapping("/purchase")
    public PurchaseResponseDTO placeOrder(@RequestBody PurchaseRequestDTO purchase, Authentication connectedUser) {
        return service.placeOrder(purchase, connectedUser);
    }
}
