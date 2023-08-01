package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p where p.category = ?1")
    List<Payment> findAllByCategory(Category category);
}
