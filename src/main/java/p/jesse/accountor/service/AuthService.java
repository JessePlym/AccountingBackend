package p.jesse.accountor.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.AuthenticationResponse;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import javax.security.auth.login.CredentialException;
import java.time.LocalDate;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordHash passwordHash;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordHash passwordHash, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordHash = passwordHash;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        // TODO: handle validation
        User user = new User(
                request.firstName(),
                request.lastName(),
                request.username(),
                passwordHash.encryptPassword(request.password()),
                LocalDate.now(),
                Role.ROLE_USER
        );
        userRepository.save(user);
        //String jwtToken = jwtService.generateToken(user);
        return null;
    }

    public ResponseEntity<String> authenticate(AuthenticationRequest request) throws CredentialException {
        if (userRepository.findByUsername(request.username()).isEmpty()) {
            throw new CredentialException("Username must not be empty!");
        }
        User user = userRepository.findByUsername(request.username()).get();
        if (!passwordHash.checkPassword(user.getPassword(), request.password())) {
            throw new CredentialException("Username or password incorrect!");
        }
        String token = jwtService.generateToken(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
