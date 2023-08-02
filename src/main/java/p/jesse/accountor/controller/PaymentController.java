package p.jesse.accountor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.service.IncomeService;
import p.jesse.accountor.service.PaymentService;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final IncomeService incomeService;

    public PaymentController(PaymentService paymentService, IncomeService incomeService) {
        this.paymentService = paymentService;
        this.incomeService = incomeService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/user/income")
    public List<Income> getAllIncomeByUser(Authentication authentication) {
        return incomeService.getAllIncomeByUser(authentication);
    }

    @GetMapping("/user/income/by-category")
    public List<Income> getAllIncomeOfUserByCategory(@RequestParam(name = "category-id", required = true) Category category, Authentication authentication) {
        return incomeService.getAllIncomeOfUserByCategory(authentication, category);
    }

    @PostMapping("/user/income/new-entry")
    public ResponseEntity<String> addNewIncomeEntry(@RequestBody Income incomeRequest, Authentication authentication) {
        return incomeService.addNewIncomeEntry(incomeRequest, authentication);
    }

    @PutMapping("/user/income/{id}")
    public ResponseEntity<String> updateIncomeEntry(@PathVariable("id") Long id, @RequestBody Income updateRequest, Authentication authentication) {
        return incomeService.updateIncomeEntry(id, updateRequest, authentication);
    }

    @DeleteMapping("/user/income/{id}")
    public ResponseEntity<String> deleteIncomeEntry(@PathVariable("id") Long id, Authentication authentication) {
        return incomeService.deleteIncomeEntry(id, authentication);
    }

}
