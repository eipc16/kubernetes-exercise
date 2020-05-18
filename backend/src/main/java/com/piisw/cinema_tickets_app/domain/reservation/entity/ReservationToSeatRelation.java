package com.piisw.cinema_tickets_app.domain.reservation.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Reservation_X_Seat")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationToSeatRelation extends AuditedObject {

    @OneToOne
    @JoinColumn(name = "reservationId")
    private Reservation reservation;

    @OneToOne
    @JoinColumn(name = "seatId")
    private Seat seat;

}
