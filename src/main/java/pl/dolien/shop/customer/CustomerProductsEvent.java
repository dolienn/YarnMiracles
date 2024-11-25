package pl.dolien.shop.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.dolien.shop.order.OrderItem;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductsEvent {
    private Customer customer;
    private Set<OrderItem> orderItems;
}
