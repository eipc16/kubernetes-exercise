package com.piisw.cinema_tickets_app.domain.movie.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Movie")
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Movie extends AuditedObject {

    @NotNull
    String imdbId;

}
