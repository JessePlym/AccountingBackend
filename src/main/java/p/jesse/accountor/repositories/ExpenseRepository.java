package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.User;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("select e from Expense e where e.user = ?1")
    List<Expense> findAllByUser(User user);

    @Query("select e from Expense e where e.user = ?1 and e.receiver = ?2 order by e.createdAt")
    List<Expense> findAllByUserAndReceiverOrderByCreatedAt(User user, String receiver);
}
