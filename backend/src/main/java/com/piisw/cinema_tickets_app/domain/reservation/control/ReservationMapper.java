package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.api.ReservationDTO;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public Reservation mapToReservation(ReservationDTO reservationDTO) {
        return Reservation.builder()
                .id(reservationDTO.getId())
                .screeningId(reservationDTO.getScreeningId())
                .reservedByUser(reservationDTO.getReservedByUser())
                .build();
    }

}
