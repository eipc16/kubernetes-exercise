package com.piisw.cinema_tickets_app.domain.movie.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private AuditedObjectSpecification<Movie> specification;

    public Movie getMovieById(Long id, ObjectState objectState) {
        return movieRepository.findOne(specification.whereIdAndObjectStateEquals(id, objectState))
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Movie.class, id, objectState));
    }

    public Movie getMovieById(Long id, Set<ObjectState> objectStates) {
        return movieRepository.findOne(specification.whereIdEqualsAndObjectStateIn(id, objectStates))
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Movie.class, id, objectStates));
    }

    public List<Movie> getMoviesByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return movieRepository.findAll(specification.whereIdAndObjectStateIn(ids, objectStates));
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
        List<Movie> moviesToRemove = movieRepository.findAll(specification.whereIdIn(ids));
        validateIfAllMoviesToRemoveExists(ids, moviesToRemove);
        moviesToRemove.forEach(movie -> movie.setObjectState(ObjectState.REMOVED));
        return movieRepository.saveAll(moviesToRemove);
    }

    private void validateIfAllMoviesToRemoveExists(Set<Long> idsOfMoviesToRemove, List<Movie> foundMovies) {
        Set<Long> idsOfNonExistingMovies = getIdsOfNonExistingMovies(idsOfMoviesToRemove, foundMovies);
        if (!idsOfNonExistingMovies.isEmpty()) {
            throw ExceptionUtils.getObjectNotFoundException(Movie.class, idsOfNonExistingMovies, ObjectState.ACTIVE);
        }
    }

    private Set<Long> getIdsOfNonExistingMovies(Set<Long> idsOfMoviesToRemove, List<Movie> foundMovies) {
        return foundMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), foundIds -> Sets.difference(idsOfMoviesToRemove, foundIds)));
    }

}
