package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.MovieRequest;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Repository.MovieRepository;
import TienToan.example.Cinema.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    public Movie getMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phim có ID: " + id));
    }

    public void deleteMovieById(Long id){
        if(!movieRepository.existsById(id)){
            throw new ResourceNotFoundException("Không tìm thấy phim để xóa với ID: " + id);
        }
        movieRepository.delete(getMovieById(id));
    }

    public Movie updateMovieById(Long id , MovieRequest req){
        Movie newmovie = movieRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phim có ID: " + id));

        newmovie.setTitle(req.title());
        newmovie.setAuthor(req.author());
        newmovie.setGenre(req.genre());
        newmovie.setDuration(req.duration());
        newmovie.setReleaseDate(req.releaseDate());
        newmovie.setDescription(req.description());

        return movieRepository.save(newmovie);
    }
}
