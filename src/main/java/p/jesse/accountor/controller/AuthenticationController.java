package p.jesse.accountor.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.records.AuthenticationRequest;
import p.jesse.accountor.records.RegisterRequest;
import p.jesse.accountor.service.AuthService;
import p.jesse.accountor.service.JwtService;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthenticationController {

    private final AuthService authService;

    private final JwtService jwtService;

    public AuthenticationController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
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
