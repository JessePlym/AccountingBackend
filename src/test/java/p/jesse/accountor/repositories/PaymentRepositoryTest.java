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
import p.jesse.accountor.entities.Payment;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {PaymentRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"p.jesse.accountor.entities"})
@DataJpaTest
@ActiveProfiles("test")
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @Test
    void shouldFindAllPaymentsByCategory() {
        Payment payment = new Payment();
        Category salary = new Category();
        salary.setPayments(List.of(payment));
        categoryRepository.save(salary);

    }
}