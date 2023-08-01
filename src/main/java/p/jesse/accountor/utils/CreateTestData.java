package p.jesse.accountor.utils;

import org.springframework.stereotype.Component;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.CategoryRepository;
import p.jesse.accountor.repositories.PaymentRepository;
import p.jesse.accountor.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;

import static p.jesse.accountor.enums.Role.ADMIN;
import static p.jesse.accountor.enums.Role.USER;

@Component
public class CreateTestData {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentRepository paymentRepository;

    private final PasswordHash passwordHash;

    public CreateTestData(UserRepository userRepository, CategoryRepository categoryRepository, PaymentRepository paymentRepository, PasswordHash passwordHash) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.paymentRepository = paymentRepository;
        this.passwordHash = passwordHash;
    }

    public void create() {
        createTestUsers();
        createTestCategories();
        createTestPayments();
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
        paymentRepository.deleteAll();

        Payment testPayment1 = new Payment(50.0, "monthly wage", LocalDate.now(), true, categoryRepository.findByName("salary").get());
        Payment testPayment2 = new Payment(100.0, "electric bill", LocalDate.now().plusDays(5), false, categoryRepository.findByName("bill").get());

        paymentRepository.saveAll(List.of(testPayment1, testPayment2));
    }

}
