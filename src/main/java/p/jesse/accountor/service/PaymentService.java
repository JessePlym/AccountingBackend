package p.jesse.accountor.service;

import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Payment;
import p.jesse.accountor.repositories.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
