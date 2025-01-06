package pl.dolien.shop.checkout;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.checkout.dto.PurchaseRequestDTO;
import pl.dolien.shop.checkout.dto.PurchaseResponseDTO;
import pl.dolien.shop.customer.Customer;
import pl.dolien.shop.order.Order;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CheckoutControllerTest {

    private static final String TEST_TRACKING_NUMBER = "1234-5678";

    @InjectMocks
    private CheckoutController checkoutController;

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private Authentication authentication;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PurchaseRequestDTO testPurchaseRequestDTO;
    private PurchaseResponseDTO testPurchaseResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkoutController).build();
        initializeTestData();
    }

    @Test
    void shouldPlaceOrder() throws Exception {
        when(checkoutService.placeOrder(any(PurchaseRequestDTO.class), any(Authentication.class)))
                .thenReturn(testPurchaseResponseDTO);

        performPurchaseRequest(testPurchaseRequestDTO)
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(testPurchaseResponseDTO)));

        verify(checkoutService).placeOrder(any(PurchaseRequestDTO.class), any(Authentication.class));
    }

    private void initializeTestData() {
        Order testOrder = Order.builder()
                .id(1L)
                .orderTrackingNumber(TEST_TRACKING_NUMBER)
                .build();

        Customer testCustomer = Customer.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("test@example.com")
                .build();

        testPurchaseRequestDTO = PurchaseRequestDTO.builder()
                .order(testOrder)
                .customer(testCustomer)
                .build();

        testPurchaseResponseDTO = PurchaseResponseDTO.builder()
                .orderTrackingNumber(TEST_TRACKING_NUMBER)
                .build();
    }

    private ResultActions performPurchaseRequest(PurchaseRequestDTO purchaseRequestDTO) throws Exception {
        return mockMvc.perform(post("/checkout/purchase")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequestDTO))
                .principal(authentication));
    }
}