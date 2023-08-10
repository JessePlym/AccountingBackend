package p.jesse.accountor.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import p.jesse.accountor.entities.User;
import p.jesse.accountor.records.NewPasswordRequest;
import p.jesse.accountor.records.UserUpdateRequest;
import p.jesse.accountor.service.UserService;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public User getUserInformation(Authentication authentication) {
        return userService.getUserInformation(authentication);
    }

    @PutMapping("/current/update-details")
    public ResponseEntity<String> updateUserDetails(@RequestBody UserUpdateRequest userUpdateRequest, Authentication authentication) {
        return userService.updateUserDetails(userUpdateRequest, authentication);
    }

    @PutMapping("/current/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid NewPasswordRequest newPasswordRequest, Authentication authentication) {
        return userService.updatePassword(newPasswordRequest, authentication);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id, Authentication authentication) {
        return userService.deleteUser(id, authentication);
    }
}
