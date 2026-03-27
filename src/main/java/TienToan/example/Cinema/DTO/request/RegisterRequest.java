package TienToan.example.Cinema.DTO.request;

import java.time.LocalDate;

public record RegisterRequest(
        String fullName,
        String email,
        String std,
        String password,
        String gender,
        String region,
        String district,
        String favoriteCinema,
        LocalDate birthday
) {
}
