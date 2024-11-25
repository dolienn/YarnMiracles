package pl.dolien.shop.checkout;

import org.springframework.security.core.Authentication;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.checkout.dto.PurchaseResponseDTO;

public interface CheckoutService {

    PurchaseResponseDTO placeOrder(PurchaseRequestDTO purchase, Authentication connectedUser);
}

