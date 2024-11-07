//package pl.dolien.shop.checkout;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.Authentication;
//import pl.dolien.shop.purchase.PurchaseRequest;
//import pl.dolien.shop.purchase.PurchaseResponse;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//public class CheckoutControllerTest {
//
//    @InjectMocks
//    private CheckoutController checkoutController;
//
//    @Mock
//    private CheckoutService service;
//
//    @Mock
//    private Authentication authentication;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void shouldReturnPurchaseResponse() {
//        PurchaseResponse expectedResponse = new PurchaseResponse("order123");
//        when(service.placeOrder(any(PurchaseRequest.class), any(Authentication.class)))
//                .thenReturn(expectedResponse);
//
//        PurchaseResponse actualResponse = checkoutController.placeOrder(PurchaseRequest.builder().build(), authentication);
//
//        assertEquals(expectedResponse, actualResponse);
//    }
//}
