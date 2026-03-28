package TienToan.example.Cinema.DTO.request;

import java.time.LocalDate;

public record MovieSearchRequest(
        String title,
        String genre,
        String city,
        LocalDate date,
        Boolean isShowing
) {}
