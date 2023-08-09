package p.jesse.accountor.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Expense;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.repositories.ExpenseRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    void shouldReturnAllExpensesOfUserByCategory() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Category existingCategory = new Category("some_category");
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        List<Expense> actualList = expenseService.getAllExpensesOfUserByCategory(new BearerTokenAuthenticationToken("ABC123"), existingCategory);
        assertThat(actualList).allMatch(expense -> existingUser.equals(expense.getUser()) && existingCategory.equals(expense.getCategory()));
        verify(expenseRepository).findAllByUserAndCategoryOrderByCreatedAt(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldReturnAllExpensesOfUserByReceiver() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        String receiver = "some_category";
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        List<Expense> actualList = expenseService.getAllExpensesOfUserByReceiver(new BearerTokenAuthenticationToken("ABC123"), receiver);
        assertThat(actualList).allMatch(expense -> existingUser.equals(expense.getUser()) && receiver.equals(expense.getReceiver()));
        verify(expenseRepository).findAllByUserAndReceiverOrderByCreatedAt(Mockito.any(), Mockito.any());
    }

    @Test
    void shouldAddNewExpenseEntry() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Expense expenseRequest = new Expense(new BigDecimal(10), "bill", LocalDate.now(), false, new Category(), "some_receiver", existingUser);
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        ResponseEntity<String> actualResponse = expenseService.addNewExpenseEntry(expenseRequest, new BearerTokenAuthenticationToken("ABC123"));
        ArgumentCaptor<Expense> expenseArgumentCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseArgumentCaptor.capture());
        Expense capturedExpense = expenseArgumentCaptor.getValue();
        assertThat(capturedExpense.getDescription()).isEqualTo(expenseRequest.getDescription());
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void shouldUpdateExpenseEntry() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Expense existingExpense = new Expense();
        existingExpense.setUser(existingUser);
        Long id = 1L;
        Expense updateRequest = new Expense(new BigDecimal(10), "bill", LocalDate.now(), false, new Category(), "some_receiver", existingUser);
        when(expenseRepository.findById(Mockito.any())).thenReturn(Optional.of(existingExpense));
        when(authChecker.isUserNotAuthenticated(Mockito.any(), Mockito.any())).thenReturn(false);
        ResponseEntity<String> actualResult = expenseService.updateExpenseEntry(id, updateRequest, new BearerTokenAuthenticationToken(existingUser.getUsername()));

        ArgumentCaptor<Expense> expenseArgumentCaptor = ArgumentCaptor.forClass(Expense.class);
        verify(expenseRepository).save(expenseArgumentCaptor.capture());
        Expense capturedExpense = expenseArgumentCaptor.getValue();
        assertThat(capturedExpense.getDescription()).isEqualTo(updateRequest.getDescription());
        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResult.getBody()).isEqualTo("Updated successfully!");
    }
    
    @Test
    void shouldReturn403IfUserNotAuthenticatedWhenUpdating() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Expense existingExpense = new Expense();
        existingExpense.setUser(existingUser);
        Long id = 1L;
        Expense updateRequest = new Expense(new BigDecimal(10), "bill", LocalDate.now(), false, new Category(), "some_receiver", existingUser);
        when(expenseRepository.findById(Mockito.any())).thenReturn(Optional.of(existingExpense));
        when(authChecker.isUserNotAuthenticated(Mockito.any(), Mockito.any())).thenReturn(true);
        ResponseEntity<String> actualResult = expenseService.updateExpenseEntry(id, updateRequest, new BearerTokenAuthenticationToken(existingUser.getUsername()));
        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldReturn404IfExpenseIdNotFoundWhenUpdating() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Expense existingExpense = new Expense();
        existingExpense.setUser(existingUser);
        Long id = 1L;
        Expense updateRequest = new Expense(new BigDecimal(10), "bill", LocalDate.now(), false, new Category(), "some_receiver", existingUser);
        when(expenseRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity<String> actualResult = expenseService.updateExpenseEntry(id, updateRequest, new BearerTokenAuthenticationToken(existingUser.getUsername()));
        assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResult.getBody()).isEqualTo("No entries found with given id");
    }
}