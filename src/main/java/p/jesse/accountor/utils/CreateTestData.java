package p.jesse.accountor.utils;

import org.springframework.stereotype.Component;
import p.jesse.accountor.entities.*;
import p.jesse.accountor.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static p.jesse.accountor.enums.Role.ADMIN;
import static p.jesse.accountor.enums.Role.USER;

@Component
public class CreateTestData {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public CreateTestData(UserRepository userRepository, CategoryRepository categoryRepository, IncomeRepository incomeRepository,
                          ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    public void create() {
        createTestUsers();
        createTestCategories();
        createTestExpenses();
        createTestIncome();
    }

    public void createTestUsers() {
        userRepository.deleteAll();

        User testUser1 = new User("Jesse", "Plym", "Plymander", "$2a$12$ezJcPT2CabmojOmqvszaOOYz1W/oah9xMYQAlYf3bCvTSQR9llYke", LocalDate.now(), USER);
        User testUser2 = new User("Admin", "Admin", "admin", "$2a$12$0g.r7XfFYfqKRv7xT2TYB.BGQVUxavASLdw12eAGhPH0fGAyHbVKK", LocalDate.now(), ADMIN);

        userRepository.saveAll(List.of(testUser1, testUser2));
    }

    public void createTestCategories() {
        categoryRepository.deleteAll();

        Category testCategory1 = new Category("Salary");
        Category testCategory2 = new Category("Bill");
        Category testCategory3 = new Category("Purchase");
        Category testCategory4 = new Category("Support");
        Category testCategory5 = new Category("Loan");
        Category testCategory6 = new Category("Rent");
        Category testCategory7 = new Category("Other");


        categoryRepository.saveAll(List.of(testCategory1, testCategory2, testCategory3, testCategory4, testCategory5, testCategory6, testCategory7));
    }

    public void createTestExpenses() {
        expenseRepository.deleteAll();

        Expense testExpense1 = new Expense(new BigDecimal(100), "lasku", LocalDate.now(), true, categoryRepository.findByName("Bill").get(), "Elisa", userRepository.findByUsername("Plymander").get());
        Expense testExpense2 = new Expense(new BigDecimal(10), "ostos", LocalDate.now().plusDays(10), false, categoryRepository.findByName("Purchase").get(), "K-market", userRepository.findByUsername("Plymander").get());

        expenseRepository.saveAll(List.of(testExpense1, testExpense2));

    }

    public void createTestIncome() {
        incomeRepository.deleteAll();

        Income testIncome1 = new Income(new BigDecimal(2000), "palkka", LocalDate.now(), false, categoryRepository.findByName("Salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");
        Income testIncome2 = new Income(new BigDecimal(2000), "palkka", LocalDate.now(), false, categoryRepository.findByName("Salary").get(), userRepository.findByUsername("admin").get(), "Caseum Oy");
        Income testIncome3 = new Income(new BigDecimal(2500), "palkka", LocalDate.now().plusMonths(1), false, categoryRepository.findByName("Salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");
        Income testIncome4 = new Income(new BigDecimal(2240), "palkka", LocalDate.now().plusMonths(2), false, categoryRepository.findByName("Salary").get(), userRepository.findByUsername("Plymander").get(), "Caseum Oy");

        incomeRepository.saveAll(List.of(testIncome1, testIncome2, testIncome3, testIncome4));
    }

}
