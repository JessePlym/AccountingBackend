package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import p.jesse.accountor.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
