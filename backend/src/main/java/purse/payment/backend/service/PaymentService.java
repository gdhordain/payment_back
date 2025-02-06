package purse.payment.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import purse.payment.backend.enums.PaymentStatus;
import purse.payment.backend.model.Payment;
import purse.payment.backend.repository.PaymentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Mono<Payment> createPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        payment.calculateTotalAmount();
        return paymentRepository.save(payment);
    }

    public Mono<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    public Flux<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Mono<Payment> updatePaymentStatus(String id, PaymentStatus newStatus) {
        return paymentRepository.findById(id).flatMap(
            payment -> {
                if (payment.getPaymentStatus() == PaymentStatus.CAPTURED) {
                    return Mono.error(new IllegalStateException("Impossible to modify a CAPTURED payment"));
                }
                if (newStatus == PaymentStatus.CAPTURED && payment.getPaymentStatus() != PaymentStatus.AUTHORIZED) {
                    return Mono.error(new IllegalStateException("Payment must be AUTHORIZED before being CAPTURED"));
                }
                payment.setPaymentStatus(newStatus);
                return paymentRepository.save(payment);
            }
        );
    }
}