package purse.payment.backend.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import purse.payment.backend.model.Payment;

@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {
}