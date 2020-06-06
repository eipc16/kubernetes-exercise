package com.piisw.cinema_tickets_app.domain.movie.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieToGenreRelation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieToGenreRelationService {

    private final MovieToGenreRelationRepository movieToGenreRelationRepository;

    public List<Genre> getGenresByMovie(Movie movie) {
        return movieToGenreRelationRepository.findAllByMovie(movie).stream()
                .map(MovieToGenreRelation::getGenre)
                .collect(Collectors.toList());
    }

    public List<MovieToGenreRelation> createRelations(Collection<Movie> movies) {
        List<MovieToGenreRelation> createdRelations = movies.stream()
                .map(this::createRelationEntities)
                .flatMap(Collection::stream)
                .collect(Collectors.toUnmodifiableList());
        return movieToGenreRelationRepository.saveAll(createdRelations);
    }

    private List<MovieToGenreRelation> createRelationEntities(Movie movie) {
        return Optional.ofNullable(movie.getGenres())
                .orElseGet(Collections::emptySet)
                .stream()
                .map(genre -> createRelation(movie, genre))
                .collect(Collectors.toUnmodifiableList());
    }

    private MovieToGenreRelation createRelation(Movie movie, Genre genre) {
        return MovieToGenreRelation.builder()
                .movie(movie)
                .genre(genre)
                .objectState(ObjectState.ACTIVE)
                .build();
    }

    public List<MovieToGenreRelation> removeRelations(Collection<Movie> movies) {
        List<MovieToGenreRelation> relationsToRemove = movieToGenreRelationRepository.findAllByMovieIn(movies);
        relationsToRemove.stream().forEach(relation -> relation.setObjectState(ObjectState.REMOVED));
        return movieToGenreRelationRepository.saveAll(relationsToRemove);
    }
}
