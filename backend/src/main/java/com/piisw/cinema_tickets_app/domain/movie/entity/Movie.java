package com.piisw.cinema_tickets_app.domain.movie.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "Movie")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Movie extends AuditedObject {

    @NotNull
    private String imdbId;

    @NotNull
    private String title;

    @NotNull
    private String year;

    @NotNull
    private String maturityRating;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    private String runTime;

    @NotNull
    private String director;

    @NotNull
    private String actors;

    @NotNull
    private String shortPlot;

    @NotNull
    private String language;

    @NotNull
    private String posterUrl;

    @NotNull
    private String country;

    @Transient
    private Set<Genre> genres;

    @OneToMany(mappedBy = MovieToGenreRelation_.MOVIE)
    private Set<MovieToGenreRelation> movieToGenreRelations;

}
