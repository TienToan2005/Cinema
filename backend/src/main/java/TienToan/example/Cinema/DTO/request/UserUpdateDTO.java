package TienToan.example.Cinema.DTO.request;

import java.time.LocalDate;

public record UserUpdateDTO(
        String fullName,
        String district,
        String region,
        LocalDate birthday,
        String favoriteCinema

) {
}
