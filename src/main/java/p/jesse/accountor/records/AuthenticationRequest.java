package p.jesse.accountor.records;

public record AuthenticationRequest(
        String username,
        String password
) {
}
