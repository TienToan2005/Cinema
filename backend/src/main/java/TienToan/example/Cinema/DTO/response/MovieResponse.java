package TienToan.example.Cinema.DTO.response;

import jakarta.persistence.Column;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class MovieResponse {
    private String title;
    private String genre;
    private String author;
    private Integer duration;
    private LocalDate releaseDate;
    private String description;
    private String posterUrl;
    private String trailerUrl;
    private Double rating;
}
