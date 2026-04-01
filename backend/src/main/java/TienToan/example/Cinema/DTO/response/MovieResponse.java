package TienToan.example.Cinema.DTO.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class MovieResponse {
    private Long id;
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
