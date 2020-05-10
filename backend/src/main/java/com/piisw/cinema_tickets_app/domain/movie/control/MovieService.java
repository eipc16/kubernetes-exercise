package com.piisw.cinema_tickets_app.domain.movie.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final String MOVIE_NOT_FOUND = "Movie with id {0} doesn''t exists";

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getMoviesByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return movieRepository.findAll(MovieSpecifications.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

    public List<Movie> createMovies(Set<String> imdbIds) {
        List<Movie> moviesToCreate = imdbIds.stream()
                .map(this::buildNewMovie)
                .collect(Collectors.toList());
        return movieRepository.saveAll(moviesToCreate);
    }

    private Movie buildNewMovie(String imbdId) {
        return Movie.builder()
                .imdbId(imbdId)
                .objectState(ObjectState.ACTIVE)
                .build();
    }

    public List<Movie> deleteMoviesByIds(Set<Long> ids) {
        List<Movie> moviesToRemove = movieRepository.findAll(MovieSpecifications.hasIdInSet(ids));
        validateIfAllMoviesToRemoveExists(ids, moviesToRemove);
        moviesToRemove.forEach(movie -> movie.setObjectState(ObjectState.REMOVED));
        return movieRepository.saveAll(moviesToRemove);
    }

    private void validateIfAllMoviesToRemoveExists(Set<Long> idsOfMoviesToRemove, List<Movie> foundMovies) {
        Set<Long> idsOfNonExistingMovies = getIdsOfNonExistingMovies(idsOfMoviesToRemove, foundMovies);
        if (!idsOfNonExistingMovies.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(MOVIE_NOT_FOUND, StringUtils.join(idsOfNonExistingMovies)));
        }
    }

    private Set<Long> getIdsOfNonExistingMovies(Set<Long> idsOfMoviesToRemove, List<Movie> foundMovies) {
        return foundMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), foundIds -> Sets.difference(idsOfMoviesToRemove, foundIds)));
    }

}
