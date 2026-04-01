package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.request.MovieRequest;
import TienToan.example.Cinema.DTO.response.MovieResponse;
import TienToan.example.Cinema.Entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    Movie toMovie(MovieRequest request);

    MovieResponse toMovieResponse(Movie movie);

    @Mapping(target = "id", ignore = true)
    void updateMovie(@MappingTarget Movie movie, MovieRequest request);
}
