//package pl.dolien.shop.checkout;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.core.Authentication;
//import pl.dolien.shop.address.Address;
//import pl.dolien.shop.customer.Customer;
//import pl.dolien.shop.customer.CustomerRepository;
//import pl.dolien.shop.order.*;
//import pl.dolien.shop.product.Product;
//import pl.dolien.shop.product.ProductRepository;
//import pl.dolien.shop.purchase.PurchaseRequest;
//
//import java.math.BigDecimal;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static pl.dolien.shop.order.OrderStatus.PENDING;
//
//public class CheckoutServiceTest {
//
//    @InjectMocks
//    private CheckoutService service;
//
//    @Mock
//    private CustomerRepository customerRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private Authentication authentication;
//
//    private Customer customer;
//
//    private Address shippingAddress;
//
//    private Address billingAddress;
//
//    private Set<OrderItem> orderItems;
//
//    private Order order;
//
//    private Product product;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        customer = Customer.builder()
//                .id(1L)
//                .firstname("testFirstname")
//                .lastname("testLastname")
//                .email("test@test.com")
//                .orders(null)
//                .build();
//
//        shippingAddress = Address.builder()
//                .id(1L)
//                .street("testStreet")
//                .city("testCity")
//                .country("testCountry")
//                .zipCode("testZipCode")
//                .order(null)
//                .build();
//
//        billingAddress = Address.builder()
//                .id(2L)
//                .street("testStreet")
//                .city("testCity")
//                .country("testCountry")
//                .zipCode("testZipCode")
//                .order(null)
//                .build();
//
//        orderItems = new HashSet<>(Set.of(
//                OrderItem.builder()
//                        .id(1L)
//                        .imageUrl("testImageUrl")
//                        .unitPrice(BigDecimal.valueOf(1))
//                        .quantity(1)
//                        .productId(1L)
//                        .order(null)
//                        .build()
//        ));
//
//        order = Order.builder()
//                .id(1L)
//                .orderTrackingNumber("testOrderTrackingNumber")
//                .totalQuantity(1)
//                .totalPrice(BigDecimal.valueOf(1))
//                .status(PENDING)
//                .dateCreated(new Date())
//                .lastUpdated(new Date())
//                .customer(customer)
//                .shippingAddress(shippingAddress)
//                .billingAddress(billingAddress)
//                .orderItems(orderItems)
//                .build();
//
//        product = Product.builder()
//                .id(1L)
//                .name("testName")
//                .description("testDescription")
//                .unitPrice(BigDecimal.valueOf(1))
//                .imageUrl("testImageUrl")
//                .unitsInStock(1)
//                .active(true)
//                .dateCreated(new Date())
//                .lastUpdated(new Date())
//                .rate(0.0)
//                .sales(1L)
//                .build();
//
//        customer.setOrders(new HashSet<>(Set.of(order)));
//        shippingAddress.setOrder(order);
//        billingAddress.setOrder(order);
//    }
//
////    @Test
////    public void shouldSuccessfullyPlaceOrder() {
////        when(authentication.getPrincipal()).thenReturn(customer);
////        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
////        when(productRepository.save(Product.builder().id(1L).build())).thenReturn(product);
////        when(customerRepository.save(customer)).thenReturn(customer);
////
////        Purchase purchase = Purchase.builder()
////                .customer(customer)
////                .shippingAddress(shippingAddress)
////                .billingAddress(billingAddress)
////                .order(order)
////                .orderItems(orderItems)
////                .build();
////
////        PurchaseResponse response = service.placeOrder(purchase, authentication);
////
////        assertNotNull(response);
////
////        verify(productRepository, times(orderItems.size())).findById(1L);
////        verify(productRepository, times(orderItems.size())).save(product);
////        verify(customerRepository, times(1)).save(customer);
////
////    }
//
//    @Test
//    public void shouldThrowExceptionWhenPurchaseIsNull() {
//        when(authentication.getPrincipal()).thenReturn(customer);
//        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));
//        when(productRepository.save(Product.builder().id(1L).build())).thenReturn(product);
//        when(customerRepository.save(customer)).thenReturn(customer);
//
//        var exp = assertThrows(NullPointerException.class, () -> service.placeOrder(null, authentication));
//
//        assertEquals("Purchase not found", exp.getMessage());
//    }
//
//    @Test
//    public void shouldThrowExceptionWhenProductIsNotFound() {
//        when(authentication.getPrincipal()).thenReturn(customer);
//        when(productRepository.findById(1L)).thenReturn(Optional.empty());
//        when(productRepository.save(Product.builder().id(1L).build())).thenReturn(product);
//        when(customerRepository.save(customer)).thenReturn(customer);
//
//        PurchaseRequest purchase = PurchaseRequest.builder()
//                .customer(customer)
//                .shippingAddress(shippingAddress)
//                .billingAddress(billingAddress)
//                .order(order)
//                .orderItems(orderItems)
//                .build();
//
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> service.placeOrder(purchase, authentication));
//
//        assertEquals("Product not found", exp.getMessage());
//    }
//}
