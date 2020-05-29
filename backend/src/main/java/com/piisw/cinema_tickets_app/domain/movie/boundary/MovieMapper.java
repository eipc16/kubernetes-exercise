package com.piisw.cinema_tickets_app.domain.movie.boundary;

import com.piisw.cinema_tickets_app.api.MovieDTO;
import com.piisw.cinema_tickets_app.api.MovieDetailsDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.boundary.GenreMapper;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieToGenreRelationService;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS_PATH;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;

@Component
@AllArgsConstructor
public class MovieMapper {

    private GenreMapper genreMapper;
    private MovieToGenreRelationService movieToGenreRelationService;

    public List<MovieDTO> mapToMovieDTOs(Collection<Movie> movies) {
        return movies.stream()
                .map(this::mapToMovieDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO mapToMovieDTO(Movie movie) {
        return MovieDTO.builder()
                .id(movie.getId())
                .imdbId(movie.getImdbId())
                .objectState(movie.getObjectState())
                .title(movie.getTitle())
                .releaseDate(movie.getReleaseDate())
                .posterUrl(movie.getPosterUrl())
                .build();
    }

    public List<MovieDetailsDTO> mapToMovieDetailsDTOs(Collection<Movie> movies) {
        return movies.stream()
                .map(this::mapToMovieDetailsDTO)
                .collect(Collectors.toList());
    }

    private MovieDetailsDTO mapToMovieDetailsDTO(Movie movie) {
        List<Genre> genres = movieToGenreRelationService.getGenresByMovie(movie);
        return MovieDetailsDTO.builder()
                .imdbId(movie.getImdbId())
                .title(movie.getTitle())
                .year(movie.getYear())
                .maturityRate(movie.getMaturityRating())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRunTime())
                .genres(genreMapper.mapToGenreNames(genres))
                .director(movie.getDirector())
                .actors(movie.getActors())
                .plot(movie.getShortPlot())
                .language(movie.getLanguage())
                .country(movie.getCountry())
                .posterUrl(movie.getPosterUrl())
                .build();
    }

    public ResourceDTO mapToResourceDTO(Movie movie) {
        return ResourceDTO.builder()
                .id(movie.getId())
                .identifier(movie.getImdbId())
                .uri(buildResourceDTOUri(movie.getId()))
                .build();
    }

    private URI buildResourceDTOUri(Long... ids) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(MovieController.MAIN_PATH)
                .path(IDS_PATH)
                .queryParam(OBJECT_STATE, ObjectState.values())
                .buildAndExpand(ids)
                .toUri();
    }

}
