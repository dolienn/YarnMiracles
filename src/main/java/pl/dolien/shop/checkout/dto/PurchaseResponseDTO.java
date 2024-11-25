package pl.dolien.shop.checkout.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseResponseDTO {

    private String orderTrackingNumber;
}
