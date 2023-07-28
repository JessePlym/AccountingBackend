package p.jesse.accountor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.AuthenticationResponse;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.service.AuthService;
import p.jesse.accountor.service.JwtService;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthService authService;

    private final JwtService jwtService;

    public AuthenticationController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthenticationRequest request) throws CredentialException {
        return authService.authenticate(request);
    }

    @PostMapping("/token")
    public String token(User user) {
        LOGGER.debug("Token requested for user: '{}'", user.getUsername());
        String token = jwtService.generateToken(user);
        LOGGER.debug("Token granted {}", token);
        return token;
    }
}
