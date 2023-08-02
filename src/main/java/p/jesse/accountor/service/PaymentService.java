package p.jesse.accountor.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.PaymentRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.util.List;

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
}
