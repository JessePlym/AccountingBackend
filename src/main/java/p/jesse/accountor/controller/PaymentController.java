package p.jesse.accountor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.service.ExpenseService;
import p.jesse.accountor.service.IncomeService;
import p.jesse.accountor.service.PaymentService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/user/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public PaymentController(PaymentService paymentService, IncomeService incomeService, ExpenseService expenseService) {
        this.paymentService = paymentService;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    @GetMapping("/all")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping
    public List<Payment> getAllPaymentsByUser(Authentication authentication) {
        return paymentService.getAllPaymentsByUser(authentication);
    }

    @GetMapping("/income")
    public List<Income> getAllIncomeByUser(Authentication authentication) {
        return incomeService.getAllIncomeByUser(authentication);
    }

    @GetMapping("/expense")
    public List<Expense> getAllExpensesByUser(Authentication authentication) {
        return expenseService.getAllExpensesByUser(authentication);
    }

    @GetMapping("/by-category")
    public List<Payment> getAllPaymentsByCategory(@RequestParam(name = "category_id", required = true) Category category, Authentication authentication) {
        return paymentService.getAllPaymentsByUserAndCategory(authentication, category);
    }

    @GetMapping("/income/by-source")
    public List<Income> getAllIncomeOfUserBySource(@RequestParam(name = "source", required = true) String source, Authentication authentication) {
        return incomeService.getAllIncomeOfUserBySource(authentication, source);
    }

    @GetMapping("/expense/by-receiver")
    public List<Expense> getAllExpensesOfUserByReceiver(@RequestParam(name = "receiver", required = true) String receiver, Authentication authentication) {
        return expenseService.getAllExpensesOfUserByReceiver(authentication, receiver);
    }

    @PostMapping("/income/new-entry")
    public ResponseEntity<String> addNewIncomeEntry(@RequestBody Income incomeRequest, Authentication authentication) {
        return incomeService.addNewIncomeEntry(incomeRequest, authentication);
    }

    @PostMapping("/expense/new-entry")
    public ResponseEntity<String> addNewExpenseEntry(@RequestBody Expense expenseRequest, Authentication authentication) {
        return expenseService.addNewExpenseEntry(expenseRequest, authentication);
    }

    @PutMapping("/income/{id}")
    public ResponseEntity<String> updateIncomeEntry(@PathVariable("id") Long id, @RequestBody Income updateRequest, Authentication authentication) {
        return incomeService.updateIncomeEntry(id, updateRequest, authentication);
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<String> updateExpenseEntry(@PathVariable("id") Long id, @RequestBody Expense updateRequest, Authentication authentication) {
        return expenseService.updateExpenseEntry(id, updateRequest, authentication);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePaymentEntry(@PathVariable("id") Long id, Authentication authentication) {
        return paymentService.deletePaymentEntry(id, authentication);
    }

}
