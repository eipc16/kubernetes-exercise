package com.piisw.cinema_tickets_app.domain.seat.boundary;

import com.piisw.cinema_tickets_app.api.SeatDTO;
import com.piisw.cinema_tickets_app.domain.seat.entity.SeatAvailabilityDetails;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {

    public SeatDTO mapToSeatDTO(SeatAvailabilityDetails seatDetails) {
        return SeatDTO.builder()
                .id(seatDetails.getSeat().getId())
                .rowNumber(seatDetails.getSeat().getRow())
                .seatNumber(seatDetails.getSeat().getNumber())
                .isAvailable(seatDetails.isAvailable())
                .build();
    }

}
