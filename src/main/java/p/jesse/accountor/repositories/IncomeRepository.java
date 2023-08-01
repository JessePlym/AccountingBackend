package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import p.jesse.accountor.entities.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
