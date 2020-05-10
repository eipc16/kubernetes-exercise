package com.piisw.cinema_tickets_app.domain.reservation.entity;

import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Reservation_X_Seat")
public class ReservationToSeatRelation {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @OneToOne
    private Reservation reservation;

    @OneToOne
    private Seat seat;

}
