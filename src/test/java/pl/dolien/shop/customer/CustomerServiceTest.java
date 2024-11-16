package pl.dolien.shop.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.order.Order;
import pl.dolien.shop.user.User;

import java.math.BigDecimal;
import java.util.HashSet;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private static final String TEST_EMAIL = "test@example.com";

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private Authentication authentication;

    private Customer testCustomer;
    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        initializeTestData();
    }

    @Test
    void shouldProcessCustomer() {
        mockExistingCustomer();

        Customer customer = customerService.processCustomer(testOrder, testCustomer, authentication);

        assertProcessedCustomer(customer);
        verifyInteractionsForExistingCustomer();
    }

    @Test
    void shouldProcessCustomerWhenCustomerDoesNotExist() {
        mockNonExistingCustomer();

        Customer customer = customerService.processCustomer(testOrder, testCustomer, authentication);

        assertProcessedCustomer(customer);
        verifyInteractionsForNonExistingCustomer();
    }

    private void initializeTestData() {
        testCustomer = Customer.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email(TEST_EMAIL)
                .orders(new HashSet<>())
                .build();

        testUser = User.builder()
                .id(1)
                .email(TEST_EMAIL)
                .build();

        testOrder = Order.builder()
                .totalPrice(BigDecimal.TEN)
                .totalQuantity(1)
                .build();
    }

    private void mockExistingCustomer() {
        when(authentication.getPrincipal()).thenReturn(testUser);
        when(customerRepository.findByEmail(anyString())).thenReturn(of(testCustomer));
    }

    private void mockNonExistingCustomer() {
        when(customerRepository.findByEmail(anyString())).thenReturn(empty());
    }

    private void assertProcessedCustomer(Customer customer) {
        assertNotNull(customer);
        assertEquals(testCustomer.getFirstname(), customer.getFirstname());
        assertEquals(testCustomer.getLastname(), customer.getLastname());
        assertEquals(testCustomer.getOrders(), customer.getOrders());
        assertEquals(1, customer.getOrders().size());
        assertEquals(testOrder, customer.getOrders().iterator().next());
    }

    private void verifyInteractionsForExistingCustomer() {
        verify(authentication, times(1)).getPrincipal();
        verify(customerRepository, times(1)).findByEmail(anyString());
    }

    private void verifyInteractionsForNonExistingCustomer() {
        verify(customerRepository, times(1)).findByEmail(anyString());
    }
}