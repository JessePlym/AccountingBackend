package p.jesse.accountor.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        String firstName,
        String lastName,
        @NotBlank @NotEmpty
        String username,
        @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters!")
        String password,
        String passwordCheck
) {
}
