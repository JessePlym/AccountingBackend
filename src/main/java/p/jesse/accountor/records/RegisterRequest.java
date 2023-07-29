package p.jesse.accountor.records;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password,
        String passwordCheck
) {
}
