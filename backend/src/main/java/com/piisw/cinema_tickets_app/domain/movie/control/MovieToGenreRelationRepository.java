package com.piisw.cinema_tickets_app.domain.movie.control;

import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieToGenreRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface MovieToGenreRelationRepository extends JpaRepository<MovieToGenreRelation, Long>, JpaSpecificationExecutor<MovieToGenreRelation> {

    List<MovieToGenreRelation> findAllByMovie(Movie movie);

    List<MovieToGenreRelation> findAllByGenre(Genre genre);

    List<MovieToGenreRelation> findAllByGenreIn(List<Genre> genre);
}