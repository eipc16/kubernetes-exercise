package com.piisw.cinema_tickets_app.domain.movie.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Movie")
public class Movie extends AuditedObject {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

}
