package p.jesse.accountor.records;

import jakarta.validation.constraints.Size;

public record NewPasswordRequest(
        @Size(min = 8, max = 16)
        String password,
        String passwordCheck
) {
}
