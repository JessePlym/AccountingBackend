package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import p.jesse.accountor.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
