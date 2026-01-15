package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {

    boolean existsByTitle(String title);
    List<Movie> findMovieByGenre(String genre);
    List<Movie> findMovieByAuthor(String author);
}
