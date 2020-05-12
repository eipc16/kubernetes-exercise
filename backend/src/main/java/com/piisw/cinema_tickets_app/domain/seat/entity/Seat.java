package com.piisw.cinema_tickets_app.domain.seat.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Seat")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Seat extends AuditedObject {

    @NotNull
    private Long row;

    @NotNull
    private Long number;

}
