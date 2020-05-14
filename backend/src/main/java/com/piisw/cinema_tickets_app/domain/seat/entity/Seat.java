package com.piisw.cinema_tickets_app.domain.seat.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "screening_room_id")
    private ScreeningRoom screeningRoom;

}
