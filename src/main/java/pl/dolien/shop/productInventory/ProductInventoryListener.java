package pl.dolien.shop.productInventory;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pl.dolien.shop.order.OrderItemsEvent;

@Component
@RequiredArgsConstructor
public class ProductInventoryListener {

    private final ProductInventoryUpdater productInventoryService;

    @KafkaListener(topics = "update-inventory", groupId = "myGroup")
    public void updateProductInventory(OrderItemsEvent event) {
        event.getItems().forEach(productInventoryService::updateProductInventory);
    }
}
