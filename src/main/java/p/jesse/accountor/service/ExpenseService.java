package p.jesse.accountor.service;

import org.springframework.stereotype.Service;
import p.jesse.accountor.repositories.ExpenseRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

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

}
