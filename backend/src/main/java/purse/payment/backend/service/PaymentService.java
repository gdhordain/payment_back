package purse.payment.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import purse.payment.backend.enums.PaymentStatus;
import purse.payment.backend.model.Payment;
import purse.payment.backend.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(Payment payment) {
        payment.setPaymentStatus(PaymentStatus.IN_PROGRESS);
        payment.calculateTotalAmount();
        return paymentRepository.save(payment);
    }

    public Optional<Payment> getPaymentById(String id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment updatePaymentStatus(String id, PaymentStatus newStatus) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        if (payment.getPaymentStatus() == PaymentStatus.CAPTURED) {
            throw new IllegalStateException("Impossible to modify a CAPTURED payment");
        }
        if (newStatus == PaymentStatus.CAPTURED && payment.getPaymentStatus() != PaymentStatus.AUTHORIZED) {
            throw new IllegalStateException("Payment must be AUTHORIZED before being CAPTURED");
        }
        payment.setPaymentStatus(newStatus);
        return paymentRepository.save(payment);
    }
}