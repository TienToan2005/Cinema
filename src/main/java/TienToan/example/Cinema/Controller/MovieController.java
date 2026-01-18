package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.ApiReponse;
import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Repository.MovieRepository;
import TienToan.example.Cinema.Service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ApiReponse<List<Movie>> getAllMovie(){
        List<Movie> movies = movieRepository.findAll();
        return ApiReponse.<List<Movie>>builder()
                .result(movies)
                .build();
    }
    @GetMapping({"/{id}"})
    public Object getMovieByID(@PathVariable Long id){
        Movie movie = movieRepository.getById(id);
        if(movie == null){
            return ResponseEntity.notFound().build();
        }
        return ApiReponse.<Movie>builder()
                .result(movie)
                .build();
    }
    @PostMapping("/create")
    public ApiReponse<Movie> createMovie(@RequestBody @Valid MovieRequest req){
        Movie newMovie =  movieService.createMovie(req);
        return ApiReponse.<Movie>builder()
                .result(newMovie)
                .message("Tạo phim thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiReponse<Movie> updateMovie(@PathVariable Long id , @RequestBody MovieRequest req){
        Movie newmovie = movieService.updateMovieById(id, req);
        return  ApiReponse.<Movie>builder()
                .result(newmovie)
                .message("Thay đổi phim thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }
}
