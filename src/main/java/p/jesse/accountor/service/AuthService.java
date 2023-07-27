package p.jesse.accountor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.enums.Role;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.AuthenticationResponse;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.repositories.UserRepository;
import p.jesse.accountor.utils.PasswordHash;

import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordHash passwordHash;

    @Autowired
    private JwtService jwtService;

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
        String jwtToken = jwtService.generateToken(user);
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }
}
