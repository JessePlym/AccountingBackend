package p.jesse.accountor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Payment;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.name = ?1")
    Optional<Category> findByName(String name);
}
