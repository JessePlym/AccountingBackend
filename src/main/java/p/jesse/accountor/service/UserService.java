package p.jesse.accountor.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.records.NewPasswordRequest;
import p.jesse.accountor.records.UserUpdateRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordHash passwordHash;

    public UserService(UserRepository userRepository, PasswordHash passwordHash) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public ResponseEntity<String> updateUserDetails(UserUpdateRequest userUpdateRequest, Authentication authentication) {
        User updatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        // if either firstname or lastname of the request is left empty, these fields will not be updated
        if (!userUpdateRequest.firstName().trim().isEmpty()) {
            updatedUser.setFirstName(userUpdateRequest.firstName());
        }
        if (!userUpdateRequest.lastName().trim().isEmpty()) {
            updatedUser.setLastName(userUpdateRequest.lastName());
        }
        userRepository.save(updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> updatePassword(NewPasswordRequest newPasswordRequest, Authentication authentication) {
        User updatedUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (!newPasswordRequest.password().equals(newPasswordRequest.passwordCheck())) {
            throw new IllegalStateException("Passwords do not match!");
        }
        updatedUser.setPassword(passwordHash.encryptPassword(newPasswordRequest.password()));
        userRepository.save(updatedUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteUser(Long id, Authentication authentication) {
        Optional<User> optionalUser = userRepository.findById(id);

        return optionalUser.map(deletedUser -> {
            if (authentication.getName().equals(deletedUser.getUsername())) {
                userRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }).orElse(new ResponseEntity<>("User with given id does not exist", HttpStatus.NOT_FOUND));
    }
}
