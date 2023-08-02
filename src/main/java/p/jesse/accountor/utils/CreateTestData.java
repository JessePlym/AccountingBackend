package p.jesse.accountor.utils;

import org.springframework.stereotype.Component;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.CategoryRepository;
import p.jesse.accountor.repositories.IncomeRepository;
import p.jesse.accountor.repositories.PaymentRepository;
import p.jesse.accountor.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static p.jesse.accountor.enums.Role.ADMIN;
import static p.jesse.accountor.enums.Role.USER;

@Component
public class CreateTestData {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentRepository paymentRepository;
    private final IncomeRepository incomeRepository;

    private final PasswordHash passwordHash;

    public CreateTestData(UserRepository userRepository, CategoryRepository categoryRepository, PaymentRepository paymentRepository, IncomeRepository incomeRepository, PasswordHash passwordHash) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.paymentRepository = paymentRepository;
        this.incomeRepository = incomeRepository;
        this.passwordHash = passwordHash;
    }

    public void create() {
        createTestUsers();
        createTestCategories();
        createTestPayments();
        createTestIncome();
    }

    public void createTestUsers() {
        userRepository.deleteAll();

        User testUser1 = new User("Jesse", "Plym", "Plymander", passwordHash.encryptPassword("jplym1996"), LocalDate.now(), USER);
        User testUser2 = new User("Admin", "Admin", "admin", passwordHash.encryptPassword("verysecret"), LocalDate.now(), ADMIN);

        userRepository.saveAll(List.of(testUser1, testUser2));
    }

    public void createTestCategories() {
        categoryRepository.deleteAll();

        Category testCategory1 = new Category("salary");
        Category testCategory2 = new Category("bill");

        categoryRepository.saveAll(List.of(testCategory1, testCategory2));
    }

    public void createTestPayments() {

    }

    public void createTestIncome() {
        incomeRepository.deleteAll();

        Income testIncome1 = new Income(new BigDecimal(2000), "palkka", LocalDate.now(), false, categoryRepository.findByName("salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");
        Income testIncome2 = new Income(new BigDecimal(2000), "palkka", LocalDate.now(), false, categoryRepository.findByName("salary").get(), userRepository.findByUsername("admin").get(), "Caseum Oy");
        Income testIncome3 = new Income(new BigDecimal(2500), "palkka", LocalDate.now().plusMonths(1), false, categoryRepository.findByName("salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");
        Income testIncome4 = new Income(new BigDecimal(2240), "palkka", LocalDate.now().plusMonths(2), false, categoryRepository.findByName("salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");

        incomeRepository.saveAll(List.of(testIncome1, testIncome2, testIncome3, testIncome4));
    }

}
