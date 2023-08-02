package p.jesse.accountor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.repositories.PaymentRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PaymentService.class})
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthChecker authChecker;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, authChecker, userRepository);
    }

    @Test
    void shouldReturnAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of());
        paymentService.getAllPayments();
        verify(paymentRepository).findAll();
    }

    @Test
    void shouldReturnAllPaymentsByUser() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        when(authChecker.extractUser(Mockito.any(), Mockito.any())).thenReturn(existingUser);
        paymentService.getAllPaymentsByUser(new BearerTokenAuthenticationToken(existingUser.getUsername()));
        verify(paymentRepository).findAllByUserOrderByCreatedAt(Mockito.any());
    }

    @Test
    void shouldDeletePaymentEntry() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Payment existingPayment = new Payment();
        existingPayment.setUser(existingUser);
        Long id = 1L;
        when(paymentRepository.findById(Mockito.any())).thenReturn(Optional.of(existingPayment));
        when(authChecker.isUserNotAuthenticated(Mockito.any(), Mockito.any())).thenReturn(false);
        ResponseEntity<String> actualResponse = paymentService.deletePaymentEntry(id, new BearerTokenAuthenticationToken("ABC123"));
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(actualResponse.getBody()).isEqualTo("Deleted successfully!");

        verify(paymentRepository).deleteById(Mockito.any());
    }

    @Test
    void shouldReturn401IfCorrectUserNotDeletingPaymentEntry() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Payment existingPayment = new Payment();
        existingPayment.setUser(existingUser);
        Long id = 1L;
        when(paymentRepository.findById(Mockito.any())).thenReturn(Optional.of(existingPayment));
        when(authChecker.isUserNotAuthenticated(Mockito.any(), Mockito.any())).thenReturn(true);
        ResponseEntity<String> actualResponse = paymentService.deletePaymentEntry(id, new BearerTokenAuthenticationToken("ABC123"));
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(paymentRepository, times(0)).deleteById(Mockito.any());
    }

    @Test
    void shouldReturn404IfGivenBadIdWhileDeletingPaymentEntry() {
        User existingUser = new User("Jesse", "Plym", "jplym", "old_password", LocalDate.now(), Role.USER);
        Payment existingPayment = new Payment();
        existingPayment.setUser(existingUser);
        Long id = 1L;
        when(paymentRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity<String> actualResponse = paymentService.deletePaymentEntry(id, new BearerTokenAuthenticationToken("ABC123"));
        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(actualResponse.getBody()).isEqualTo("No entries found with given id");

        verify(paymentRepository, times(0)).deleteById(Mockito.any());
    }
}