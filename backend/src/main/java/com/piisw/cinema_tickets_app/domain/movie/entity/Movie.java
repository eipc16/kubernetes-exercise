package com.piisw.cinema_tickets_app.domain.movie.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Table(name = "Movie")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends AuditedObject {

    @NotNull
    String imdbId;

    @NotNull
    String title;

    @NotNull
    String year;

    @NotNull
    String maturityRating;

    @NotNull
    Instant releaseDate;

    @NotNull
    String runTime;

    @NotNull
    String director;

    @NotNull
    String actors;

    @NotNull
    String shortPlot;

    @NotNull
    String language;

    @NotNull
    String posterUrl;

    @NotNull
    String country;
}
