package TienToan.example.Cinema.Service;

import TienToan.example.Cinema.DTO.request.MovieRequest;
import TienToan.example.Cinema.DTO.request.MovieSearchRequest;
import TienToan.example.Cinema.DTO.response.MovieResponse;
import TienToan.example.Cinema.DTO.response.PageResponse;
import TienToan.example.Cinema.Entity.Movie;
import TienToan.example.Cinema.Entity.MovieSpecification;
import TienToan.example.Cinema.Mapper.MovieMapper;
import TienToan.example.Cinema.Repository.MovieRepository;
import TienToan.example.Cinema.enums.ErrorCode;
import TienToan.example.Cinema.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {
    MovieRepository movieRepository;
    MovieMapper movieMapper;

    @Transactional
    public MovieResponse createMovie(MovieRequest req) {
        if (movieRepository.existsByTitle(req.title())) {
            throw new AppException(ErrorCode.MOVIE_EXISTED);
        }

        Movie movie = movieMapper.toMovie(req);
        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }

    public List<MovieResponse> getAllMovie() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }
    public PageResponse<MovieResponse> searchMovies(MovieSearchRequest request, Pageable pageable) {

        Specification<Movie> spec = Specification.where(MovieSpecification.filter(request));

        Page<Movie> moviePage = movieRepository.findAll(spec, pageable);

        return PageResponse.<MovieResponse>builder()
                .currentPage(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPages(moviePage.getTotalPages())
                .totalElements(moviePage.getTotalElements())
                .data(moviePage.getContent().stream().map(movieMapper::toMovieResponse).toList())
                .build();
    }
    public MovieResponse getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(movieMapper::toMovieResponse)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    @Transactional
    public void deleteMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    @Transactional
    public MovieResponse updateMovieById(Long id, MovieRequest req) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        movieMapper.updateMovie(movie, req);

        return movieMapper.toMovieResponse(movieRepository.save(movie));
    }
}