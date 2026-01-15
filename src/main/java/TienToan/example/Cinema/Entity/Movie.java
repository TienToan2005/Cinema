package TienToan.example.Cinema.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;
    private String author;

    private Integer duration;
    private LocalDate releaseDate;

    @Column(columnDefinition = "Text")
    private String description;

}