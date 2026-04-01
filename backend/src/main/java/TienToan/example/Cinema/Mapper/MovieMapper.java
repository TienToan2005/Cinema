package TienToan.example.Cinema.Mapper;

import TienToan.example.Cinema.DTO.request.MovieRequest;
import TienToan.example.Cinema.DTO.response.MovieResponse;
import TienToan.example.Cinema.Entity.Movie;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(target = "id", source = "id")
    MovieResponse toMovieResponse(Movie movie);

    Movie toMovie(MovieRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateMovie(@MappingTarget Movie movie, MovieRequest request);
}