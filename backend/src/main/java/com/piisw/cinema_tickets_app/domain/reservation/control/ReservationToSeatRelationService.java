package com.piisw.cinema_tickets_app.domain.reservation.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReservationToSeatRelationService {

    @Autowired
    private ReservationToSeatRelationRepository reservationToSeatRelationRepository;

    @Autowired
    private ReservationToRelationSpecification specification;

}