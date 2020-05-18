package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class ReservationToSeatRelationService {

    @Autowired
    private ReservationToSeatRelationRepository reservationToSeatRelationRepository;

    @Autowired
    private ReservationToRelationSpecification specification;

    @Autowired
    private AuditedObjectService auditedObjectService;

    public List<ReservationToSeatRelation> getReservationToSeatRelationsReservations(List<Reservation> reservations, Set<ObjectState> objectStates) {
        Set<Long> reservationsIds = auditedObjectService.toSetOfIds(reservations);
        return reservationToSeatRelationRepository.findAll(specification.whereReservationIdAndObjectStateIn(reservationsIds, objectStates));
    }

}
