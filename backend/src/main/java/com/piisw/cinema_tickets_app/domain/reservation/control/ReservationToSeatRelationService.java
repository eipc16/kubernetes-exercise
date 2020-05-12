package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationToSeatRelationService {

    @Autowired
    private ReservationToSeatRelationRepository reservationToSeatRelationRepository;

    @Autowired
    private ReservationToRelationSpecification specification;

}
