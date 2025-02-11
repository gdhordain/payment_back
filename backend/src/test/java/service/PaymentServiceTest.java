package service;

import org.bson.codecs.ObjectIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import purse.payment.backend.BackendApplication;
import purse.payment.backend.enums.PaymentStatus;
import purse.payment.backend.model.Payment;
import purse.payment.backend.model.Product;
import purse.payment.backend.repository.PaymentRepository;
import purse.payment.backend.service.PaymentService;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest(classes = BackendApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class PaymentServiceTest {

//    @Autowired
    private PaymentService paymentService;
//    @Autowired
//    @Mock
    private PaymentRepository paymentRepository = mock(PaymentRepository.class);

    @BeforeEach
    void setup() {
        paymentService = new PaymentService(paymentRepository);
    }

    @Nested
    public class createPaymentTest {

        @DisplayName("Create payment ok")
        @Test
        void createPaymentTest_ok() {
            // Given
            Payment payment = new Payment();
            Product product = new Product("1", "pc", 2, new BigDecimal(499.99));
            List<Product> productList = new ArrayList<>();
            productList.add(product);
            payment.setProductList(productList);


            // When
            when(paymentRepository.save(payment)).thenReturn(Mono.just(payment));
            // Then
            var newPayment = paymentService.createPayment(payment).block();
            // Assert
            assertEquals(PaymentStatus.IN_PROGRESS, newPayment.getPaymentStatus());
            assertEquals(new BigDecimal(499.99).multiply(new BigDecimal(2)), newPayment.getAmount());

//            assertNotNull(paymentRepository.findById(newPayment.getPaymentId()));

            verify(paymentRepository, times(1)).save(payment);
        }
    }
}
