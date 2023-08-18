package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.User;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("select i from Income i where i.user = ?1")
    List<Income> findAllByUser(User user);

    @Query("select i from Income i where i.user = ?1 and i.source = ?2 order by i.createdAt")
    List<Income> findAllByUserAndSourceOrderByCreatedAt(User user, String source);
}
