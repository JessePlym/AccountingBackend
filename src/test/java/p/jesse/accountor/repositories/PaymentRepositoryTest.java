package p.jesse.accountor.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.*;
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
class PaymentRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindAllPaymentsByUser() {
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
        Payment payment = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category,
                user,
                "no source"
        );
        paymentRepository.save(payment);

        Payment actualIncome = paymentRepository.findAllByUserOrderByCreatedAt(user).get(0);

        assertThat(actualIncome).isEqualTo(payment);
        assertThat(actualIncome.getUser().getUsername()).isEqualTo(user.getUsername());
        assertThat(paymentRepository.findAllByUserOrderByCreatedAt(user)).hasSize(1);
    }

    @Test
    void shouldFindAllPaymentsByCategoryOrderedByDate() {
        User user = new User(
                "Jesse",
                "Plym",
                "username",
                "asd123456",
                LocalDate.now(),
                Role.USER);
        userRepository.save(user);
        Category category1 = new Category("some_category1");
        Category category2 = new Category("some_category2");
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        Payment income1 = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now().plusDays(10),
                false,
                category1,
                user,
                "no source"
        );
        Payment income2 = new Income(
                new BigDecimal(200),
                "bill",
                LocalDate.now(),
                false,
                category1,
                user,
                "no source"
        );
        Payment expense1 = new Expense(
                new BigDecimal(500),
                "purcahase",
                LocalDate.now(),
                true,
                category2,
                "no receiver",
                user
        );
        paymentRepository.saveAll(List.of(income1, income2, expense1));
        List<Payment> actualPaymentList = paymentRepository.findAllByUserAndCategoryOrderByCreatedAt(user, category1);
        assertThat(actualPaymentList).hasSize(2);
        assertThat(actualPaymentList.get(0)).isEqualTo(income2);
        assertThat(actualPaymentList).allMatch(income -> user.equals(income.getUser()));
    }
}