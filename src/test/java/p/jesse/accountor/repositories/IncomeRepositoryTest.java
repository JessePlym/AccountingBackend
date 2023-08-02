package p.jesse.accountor.repositories;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {IncomeRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"p.jesse.accountor.entities"})
@DataJpaTest
@ActiveProfiles("test")
class IncomeRepositoryTest {

    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        incomeRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindAllIncomeByUser() {
        User user = new User(
                "Jesse",
                "Plym",
                "username",
                "asd123456",
                LocalDate.now(),
                Role.USER);
        userRepository.save(user);
        Category category = new Category("some_category");
        categoryRepository.save(category);
        Income income = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                "no source",
                user
        );
        incomeRepository.save(income);

        Income actualIncome = incomeRepository.findAllByUser(user).get(0);

        assertThat(actualIncome).isEqualTo(income);
        assertThat(actualIncome.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(incomeRepository.findAllByUser(user)).hasSize(1);
    }

    @Test
    void shouldFindAllIncomeByCategoryOrderedByDate() {
        User user = new User(
                "Jesse",
                "Plym",
                "username",
                "asd123456",
                LocalDate.now(),
                Role.USER);
        userRepository.save(user);
        Category category = new Category("some_category");
        categoryRepository.save(category);
        Income income1 = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now().plusDays(10),
                false,
                category,
                "no source",
                user
        );
        Income income2 = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                "no source",
                user
        );
        incomeRepository.saveAll(List.of(income1, income2));
        List<Income> actualIncomeList = incomeRepository.findAllByUserAndCategoryOrderByCreatedAt(user, category);
        assertThat(actualIncomeList).hasSize(2);
        assertThat(actualIncomeList.get(0)).isEqualTo(income2);
        assertThat(actualIncomeList).allMatch(income -> user.equals(income.getUser()));
    }
}