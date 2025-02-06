package purse.payment.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purse.payment.backend.enums.PaymentStatus;
import purse.payment.backend.model.Payment;
import purse.payment.backend.service.PaymentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Payment> createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Payment>> getPaymentById(@PathVariable String id) {
        return paymentService.getPaymentById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PutMapping("/{id}/status")
    public Mono<Payment> updatePaymentStatus(@PathVariable String id, @RequestParam PaymentStatus newStatus) {
        return paymentService.updatePaymentStatus(id, newStatus);
    }
}
