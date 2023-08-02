package p.jesse.accountor.service;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.Category;
import p.jesse.accountor.entities.Income;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.repositories.IncomeRepository;
import p.jesse.accountor.repositories.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public List<Income> getAllIncomeByUser(Authentication authentication) {
        User user = extractUser(authentication, userRepository);
        return incomeRepository.findAllByUser(user);
    }

    public List<Income> getAllIncomeOfUserByCategory(Authentication authentication, Category category) {
        User user = extractUser(authentication, userRepository);
        return incomeRepository.findAllByUserAndCategoryOrderByCreatedAt(user, category);
    }

    public List<Income> getAllIncomeOfUserBySource(Authentication authentication, String source) {
        User user = extractUser(authentication, userRepository);
        return incomeRepository.findAllByUserAndSourceOrderByCreatedAt(user, source.replace("_", " "));
    }

    @Transactional
    public ResponseEntity<String> addNewIncomeEntry(Income incomeRequest, Authentication authentication) {
        User user = extractUser(authentication, userRepository);

        Income savedIncome = new Income();
        savedIncome.setAmount(incomeRequest.getAmount());
        savedIncome.setDescription(incomeRequest.getDescription());
        savedIncome.setCreatedAt(LocalDate.now());
        savedIncome.setUpdatedAt(LocalDate.now());
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
            if (isCorrectUserAuthenticated(updatedIncome.getUser().getUsername(), authentication)) {
                return new ResponseEntity<String>(HttpStatus.FORBIDDEN);
            } else {
                updatedIncome.setUpdatedAt(LocalDate.now());
                updatedIncome.setAmount(updateRequest.getAmount());
                updatedIncome.setDescription(updateRequest.getDescription());
                updatedIncome.setSource(updateRequest.getSource());
                updatedIncome.setCategory(updateRequest.getCategory());
                incomeRepository.save(updatedIncome);
                return new ResponseEntity<String>(HttpStatus.OK);
            }
        }).orElse(new ResponseEntity<>("No entries found with given id", HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<String> deleteIncomeEntry(Long id, Authentication authentication) {
        Optional<Income> optionalIncome = incomeRepository.findById(id);

        return optionalIncome.map(deletedIncome -> {
            if (isCorrectUserAuthenticated(deletedIncome.getUser().getUsername(), authentication)) {
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
            }
            incomeRepository.deleteById(deletedIncome.getId());
            return new ResponseEntity<>("Deleted successfully!", HttpStatus.NO_CONTENT);
        }).orElse(new ResponseEntity<>("No entries found with given id", HttpStatus.NOT_FOUND));

    }

    private static User extractUser(Authentication authentication, UserRepository userRepository) {
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private static boolean isCorrectUserAuthenticated(String username, Authentication authentication) {
        return !username.equals(authentication.getName());
    }
}
