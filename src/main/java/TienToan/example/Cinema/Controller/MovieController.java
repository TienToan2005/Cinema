package TienToan.example.Cinema.Controller;

import TienToan.example.Cinema.DTO.request.MovieSearchRequest;
import TienToan.example.Cinema.DTO.response.ApiResponse;
import TienToan.example.Cinema.DTO.request.MovieRequest;
import TienToan.example.Cinema.DTO.response.MovieResponse;
import TienToan.example.Cinema.DTO.response.PageResponse;
import TienToan.example.Cinema.Service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/movies")
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/search")
    public ApiResponse<PageResponse<MovieResponse>> search(
            @RequestBody MovieSearchRequest request,
            @PageableDefault(sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.<PageResponse<MovieResponse>>builder()
                .data(movieService.searchMovies(request,pageable))
                .build();
    }
    @GetMapping
    public ApiResponse<List<MovieResponse>> getAllMovie(){
        return ApiResponse.<List<MovieResponse>>builder()
                .data(movieService.getAllMovie())
                .build();
    }
    @GetMapping({"/{id}"})
    public ApiResponse<MovieResponse> getMovieByID(@PathVariable Long id){
        return ApiResponse.<MovieResponse>builder()
                .data(movieService.getMovieById(id))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<MovieResponse> createMovie(@RequestBody @Valid MovieRequest req){
        return ApiResponse.<MovieResponse>builder()
                .data(movieService.createMovie(req))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<MovieResponse> updateMovie(@PathVariable Long id , @RequestBody MovieRequest req){
        return  ApiResponse.<MovieResponse>builder()
                .data(movieService.updateMovieById(id, req))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id){
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }
}
