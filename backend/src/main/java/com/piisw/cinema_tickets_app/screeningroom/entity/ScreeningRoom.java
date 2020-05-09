package com.piisw.cinema_tickets_app.screeningroom.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "ScreeningRoom")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningRoom extends AuditedObject {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @NotNull
    @Positive
    private Long number;

    @NotNull
    @Positive
    private Long numberOfSeats;

    public Long getId() {
        return id;
    }

    public Long getNumber() {
        return number;
    }

    public Long getNumberOfSeats() {
        return numberOfSeats;
    }

}
