package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie createMovie(MovieRequest req){
        if(movieRepository.existsByTitle(req.title())){
            throw new RuntimeException("Phim này đã tồn tại rồi!");
        }

        Movie movie = new Movie();

        movie.setTitle(req.title());
        movie.setGenre(req.genre());
        movie.setAuthor(req.author());
        movie.setDuration(req.duration());
        movie.setReleaseDate(req.releaseDate());
        movie.setDescription(req.description());

        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovie(){
        return movieRepository.findAll();
    }

    public Movie getMovieByID(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim có ID: " + id));
    }

}
