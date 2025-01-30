package purse.payment.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import purse.payment.backend.enums.PaymentMethod;
import purse.payment.backend.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "payment")
public class Payment {

    @Id
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<Product> productList;

    public void calculateTotalAmount() {
        if (productList != null) {
            this.amount = productList.stream()
                    .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.amount = BigDecimal.ZERO;
        }
    }
}
