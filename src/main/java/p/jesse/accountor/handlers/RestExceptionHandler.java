package p.jesse.accountor.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.CredentialException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(CredentialException.class)
    public ResponseEntity<String> handleCredentialException(CredentialException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
