package purse.payment.backend.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    private String reference;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
