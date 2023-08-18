package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query("select p from Payment p where p.user = ?1 order by p.createdAt")
    List<Payment> findAllByUserOrderByCreatedAt(User user);

    @Query("select p from Payment p where p.user = ?1 and p.category = ?2 order by p.createdAt")
    List<Payment> findAllByUserAndCategoryOrderByCreatedAt(User user, Category category);

}
