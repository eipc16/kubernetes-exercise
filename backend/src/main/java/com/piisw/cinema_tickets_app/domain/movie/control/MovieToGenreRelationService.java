package com.piisw.cinema_tickets_app.domain.movie.control;

import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieToGenreRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieToGenreRelationService {

    @Autowired
    private MovieToGenreRelationRepository movieToGenreRelationRepository;

    public List<Genre> getGenresByMovie(Movie movie) {
        return movieToGenreRelationRepository.findAllByMovie(movie).stream()
                .map(MovieToGenreRelation::getGenre)
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenre(Genre genre) {
        return movieToGenreRelationRepository.findAllByGenre(genre).stream()
                .map(MovieToGenreRelation::getMovie)
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByGenres(List<Genre> genres) {
        return movieToGenreRelationRepository.findAllByGenreIn(genres).stream()
                .map(MovieToGenreRelation::getMovie)
                .collect(Collectors.toList());
    }
}
