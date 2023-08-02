package p.jesse.accountor.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.repositories.ExpenseRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ExpenseService.class})
@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthChecker authChecker;
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        expenseService = new ExpenseService(expenseRepository, userRepository, authChecker);
    }

    @Test
    void shouldReturnAllExpensesByUser() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        List<Expense> actualList = expenseService.getAllExpensesByUser(new BearerTokenAuthenticationToken(existingUser.getUsername()));
        assertThat(actualList).allMatch(expense -> existingUser.equals(expense.getUser()));
        verify(expenseRepository).findAllByUser(Mockito.any());
    }

    @Test
    void getAllExpensesOfUserByCategory() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Category existingCategory = new Category("some_category");
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        List<Expense> actualList = expenseService.getAllExpensesOfUserByCategory(new BearerTokenAuthenticationToken("ABC123"), existingCategory);
        assertThat(actualList).allMatch(expense -> existingUser.equals(expense.getUser()) && existingCategory.equals(expense.getCategory()));
        verify(expenseRepository).findAllByUserAndCategoryOrderByCreatedAt(Mockito.any(), Mockito.any());
    }

    @Test
    void getAllExpensesOfUserByReceiver() {
    }

    @Test
    void addNewExpenseEntry() {
    }

    @Test
    void updateExpenseEntry() {
    }
}