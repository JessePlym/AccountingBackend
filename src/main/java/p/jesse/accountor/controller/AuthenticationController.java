package p.jesse.accountor.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.service.AuthService;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthenticationRequest request) throws CredentialException {
        return authService.authenticate(request);
    }
}
