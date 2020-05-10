package com.piisw.cinema_tickets_app.domain.movie.boundary;

import com.piisw.cinema_tickets_app.api.MovieDTO;
import com.piisw.cinema_tickets_app.api.MovieDetailsDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.client.OpenMovieDatabaseClient;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS_PATH;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;

@Component
public class MovieMapper {

    @Autowired
    private OpenMovieDatabaseClient openMovieDatabaseClient;

    public List<MovieDTO> mapToMovieDTOs(Collection<Movie> movies) {
        Map<String, MovieDetailsDTO> movieDetailsById = movies.stream()
                .map(Movie::getImdbId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), this::getMovieDetailsByImbdIds));
        return movies.stream()
                .map(movie -> mapToMovieDTO(movie, movieDetailsById.getOrDefault(movie.getImdbId(), null)))
                .collect(Collectors.toList());
    }

    private Map<String, MovieDetailsDTO> getMovieDetailsByImbdIds(Set<String> ids) {
        return openMovieDatabaseClient.getMovieDetailsByImdbIds(ids).stream()
                .collect(Collectors.toMap(MovieDetailsDTO::getImdbId, Function.identity()));
    }

    private MovieDTO mapToMovieDTO(Movie movie, MovieDetailsDTO movieDetailsDTO) {
        return MovieDTO.builder()
                .id(movie.getId())
                .imdbId(movie.getImdbId())
                .objectState(movie.getObjectState())
                .details(movieDetailsDTO)
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
