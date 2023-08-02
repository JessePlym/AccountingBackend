package p.jesse.accountor.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.PaymentRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AuthChecker authChecker;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, AuthChecker authChecker, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.authChecker = authChecker;
        this.userRepository = userRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getAllPaymentsByUser(Authentication authentication) {
        User user = authChecker.extractUser(authentication, userRepository);
        return paymentRepository.findAllByUserOrderByCreatedAt(user);
    }

    public ResponseEntity<String> deletePaymentEntry(Long id, Authentication authentication) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);

        return optionalPayment.map(deletedPayment -> {
            if (authChecker.isUserNotAuthenticated(deletedPayment.getUser().getUsername(), authentication)) {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            paymentRepository.deleteById(deletedPayment.getId());
            return new ResponseEntity<>("Deleted successfully!", HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>("No entries found with given id", HttpStatus.NOT_FOUND));

    }
}
