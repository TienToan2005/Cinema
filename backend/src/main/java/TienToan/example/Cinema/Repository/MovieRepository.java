package TienToan.example.Cinema.Repository;

import TienToan.example.Cinema.Entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> , JpaSpecificationExecutor<Movie> {

    boolean existsByTitle(String title);

    @Query("select distinct m from Movie m join Schedule s on m.id = s.movie.id " +
            "where m.releaseDate <= :now and s.startTime >= :now")
    List<Movie> findShowingNow(LocalDateTime now);

    List<Movie> findByReleaseDateAfter(LocalDate date);
}
