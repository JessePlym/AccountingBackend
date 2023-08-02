package p.jesse.accountor.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {ExpenseRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"p.jesse.accountor.entities"})
@DataJpaTest
@ActiveProfiles("test")
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void ShouldFindAllExpensesByUser() {
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
        Expense expense = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                "some_receiver",
                user
        );
        expenseRepository.save(expense);

        Expense actualExpense = expenseRepository.findAllByUser(user).get(0);

        assertThat(actualExpense).isEqualTo(expense);
        assertThat(actualExpense.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(expenseRepository.findAllByUser(user)).hasSize(1);
    }

    @Test
    void shouldFindExpensesByUserAndCategoryOrderByDate() {
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
        Expense expense1 = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now().plusDays(5),
                false,
                category,
                "some_receiver",
                user
        );
        Expense expense2 = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                "some_receiver",
                user
        );
        expenseRepository.saveAll(List.of(expense1, expense2));
        List<Expense> actualExpenseList = expenseRepository.findAllByUserAndCategoryOrderByCreatedAt(user, category);
        assertThat(actualExpenseList).hasSize(2);
        assertThat(actualExpenseList.get(0)).isEqualTo(expense2);
        assertThat(actualExpenseList).allMatch(expense -> user.equals(expense.getUser()));
    }

    @Test
    void shouldFindExpensesByUserAndReceiverOrderedByDate() {
        User user = new User(
                "Jesse",
                "Plym",
                "username",
                "asd123456",
                LocalDate.now(),
                Role.USER);
        userRepository.save(user);
        String receiver = "some_receiver";
        Category category = new Category("some_category");
        categoryRepository.save(category);
        Expense expense1 = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now().plusDays(5),
                false,
                category,
                receiver,
                user
        );
        Expense expense2 = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                receiver,
                user
        );
        Expense expense3 = new Expense(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                "other_receiver",
                user
        );
        expenseRepository.saveAll(List.of(expense1, expense2, expense3));
        List<Expense> actualExpenseList = expenseRepository.findAllByUserAndReceiverOrderByCreatedAt(user, receiver);
        assertThat(actualExpenseList).hasSize(2);
        assertThat(actualExpenseList.get(0)).isEqualTo(expense2);
        assertThat(actualExpenseList).allMatch(expense -> user.equals(expense.getUser()));
    }
}