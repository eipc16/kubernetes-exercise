package com.piisw.cinema_tickets_app.domain.seat.boundary;

import com.piisw.cinema_tickets_app.api.SeatDTO;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatDTO mapToSeatDTO(Seat seat, boolean isAvailable) {
        return SeatDTO.builder()
                .id(seat.getId())
                .rowNumber(seat.getRow())
                .seatNumber(seat.getNumber())
                .isAvailable(isAvailable)
                .build();
    }

}
