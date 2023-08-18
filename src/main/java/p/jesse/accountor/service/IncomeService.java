package p.jesse.accountor.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.IncomeRepository;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.AuthChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final AuthChecker authChecker;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository, AuthChecker authChecker) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.authChecker = authChecker;
    }

    public List<Income> getAllIncomeByUser(Authentication authentication) {
        User user = authChecker.extractUser(authentication, userRepository);
        return incomeRepository.findAllByUser(user);
    }

    public List<Income> getAllIncomeOfUserBySource(Authentication authentication, String source) {
        User user = authChecker.extractUser(authentication, userRepository);
        return incomeRepository.findAllByUserAndSourceOrderByCreatedAt(user, source.trim().replace("_", " "));
    }

    @Transactional
    public ResponseEntity<String> addNewIncomeEntry(Income incomeRequest, Authentication authentication) {
        User user = authChecker.extractUser(authentication, userRepository);

        Income savedIncome = new Income();
        savedIncome.setAmount(incomeRequest.getAmount());
        savedIncome.setDescription(incomeRequest.getDescription());
        savedIncome.setCreatedAt(LocalDate.now());
        savedIncome.setUpdatedAt(LocalDate.now());
        savedIncome.setContinuous(incomeRequest.isContinuous());
        savedIncome.setCategory(incomeRequest.getCategory());
        savedIncome.setSource(incomeRequest.getSource());
        savedIncome.setUser(user);

        incomeRepository.save(savedIncome);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateIncomeEntry(Long id, Income updateRequest, Authentication authentication) {
        Optional<Income> optionalIncome = incomeRepository.findById(id);

        return optionalIncome.map(updatedIncome -> {
            if (authChecker.isUserNotAuthenticated(updatedIncome.getUser().getUsername(), authentication)) {
                return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
            } else {
                updatedIncome.setUpdatedAt(LocalDate.now());
                updatedIncome.setAmount(updateRequest.getAmount());
                updatedIncome.setDescription(updateRequest.getDescription());
                updatedIncome.setContinuous(updateRequest.isContinuous());
                updatedIncome.setSource(updateRequest.getSource());
                updatedIncome.setCategory(updateRequest.getCategory());
                incomeRepository.save(updatedIncome);
                return new ResponseEntity<>("Updated successfully!", HttpStatus.OK);
            }
        }).orElse(new ResponseEntity<>("No entries found with given id", HttpStatus.NOT_FOUND));
    }

}
