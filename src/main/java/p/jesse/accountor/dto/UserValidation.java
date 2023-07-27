package p.jesse.accountor.dto;

public record UserValidation(
        String username,
        String password,
        String passwordCheck
) {
}
