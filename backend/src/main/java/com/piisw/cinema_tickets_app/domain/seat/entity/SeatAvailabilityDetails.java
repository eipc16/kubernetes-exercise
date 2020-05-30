package com.piisw.cinema_tickets_app.domain.seat.entity;

import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationState;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatAvailabilityDetails {

    private Seat seat;
    private ReservationState reservationState;

}
