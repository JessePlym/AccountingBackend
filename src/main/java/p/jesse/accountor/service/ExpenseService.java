package p.jesse.accountor.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.ExpenseRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final AuthChecker authChecker;

    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, AuthChecker authChecker) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.authChecker = authChecker;
    }

    public List<Expense> getAllExpensesByUser(Authentication authentication) {
        User user = authChecker.extractUser(authentication, userRepository);
        return expenseRepository.findAllByUser(user);
    }

    public List<Expense> getAllExpensesOfUserByCategory(Authentication authentication, Category category) {
        User user = authChecker.extractUser(authentication, userRepository);
        return expenseRepository.findAllByUserAndCategoryOrderByCreatedAt(user, category);
    }

    public List<Expense> getAllExpensesOfUserByReceiver(Authentication authentication, String receiver) {
        User user = authChecker.extractUser(authentication, userRepository);
        return expenseRepository.findAllByUserAndReceiverOrderByCreatedAt(user, receiver.trim().replace("_", " "));
    }

    @Transactional
    public ResponseEntity<String> addNewExpenseEntry(Expense expenseRequest, Authentication authentication) {
        User user = authChecker.extractUser(authentication, userRepository);

        Expense savedExpense = new Expense();
        savedExpense.setAmount(expenseRequest.getAmount());
        savedExpense.setDescription(expenseRequest.getDescription());
        savedExpense.setCreatedAt(LocalDate.now());
        savedExpense.setUpdatedAt(LocalDate.now());
        savedExpense.setContinuous(expenseRequest.isContinuous());
        savedExpense.setCategory(expenseRequest.getCategory());
        savedExpense.setReceiver(expenseRequest.getReceiver());
        savedExpense.setUser(user);

        expenseRepository.save(savedExpense);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateExpenseEntry(Long id, Expense updateRequest, Authentication authentication) {
        Optional<Expense> optionalExpense = expenseRepository.findById(id);

        return optionalExpense.map(updatedExpense -> {
            if (authChecker.isUserNotAuthenticated(updatedExpense.getUser().getUsername(), authentication)) {
                return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
            } else {
                updatedExpense.setUpdatedAt(LocalDate.now());
                updatedExpense.setAmount(updateRequest.getAmount());
                updatedExpense.setDescription(updateRequest.getDescription());
                updatedExpense.setContinuous(updatedExpense.isContinuous());
                updatedExpense.setReceiver(updatedExpense.getReceiver());
                updatedExpense.setCategory(updateRequest.getCategory());
                expenseRepository.save(updatedExpense);
                return new ResponseEntity<>("Updated successfully!", HttpStatus.OK);
            }
        }).orElse(new ResponseEntity<>("No entries found with given id", HttpStatus.NOT_FOUND));
    }

}
