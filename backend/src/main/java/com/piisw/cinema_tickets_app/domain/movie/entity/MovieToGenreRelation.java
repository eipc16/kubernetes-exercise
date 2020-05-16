package com.piisw.cinema_tickets_app.domain.movie.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Movie_X_Genre")
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovieToGenreRelation extends AuditedObject {

    @OneToOne
    @JoinColumn(name = "movieId")
    private Movie movie;

    @OneToOne
    @JoinColumn(name = "genreId")
    private Genre genre;

}