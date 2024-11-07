package pl.dolien.shop.payment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfo {
    private int amount;
    private String currency;
    private String receiptEmail;

}
