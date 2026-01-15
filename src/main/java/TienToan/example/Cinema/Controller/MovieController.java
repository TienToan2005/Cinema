package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Repository.MovieRepository;
import TienToan.example.Cinema.Service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/movies")
public class MovieController {
    private final MovieService movieService;
    private final MovieRepository movieRepository;

    @GetMapping
    public ResponseEntity<?> getAllMovie(){

        List<Movie> movies = movieRepository.findAll();
        return ResponseEntity.ok(movies);
    }
    @GetMapping({"/{id}"})
    public ResponseEntity<?> getMovieByID(@PathVariable Long id){
        Movie movie = movieRepository.getById(id);
        if(movie == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createMovie(@RequestBody @Valid MovieRequest req){
        if(movieRepository.existsByTitle(req.title())){
            return ResponseEntity.badRequest().body("Movie đã tồn tại!");
        }
        movieService.createMovie(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(req);
    }

}
