package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReservationToRelationSpecification extends AuditedObjectSpecification<ReservationToSeatRelation> {

    public Specification<ReservationToSeatRelation> whereReservationIdAndObjectStateIn(Set<Long> reservationIds, Set<ObjectState> objectStates) {
        return whereReservationIdIn(reservationIds).and(whereObjectStateIn(objectStates));
    }

    public Specification<ReservationToSeatRelation> whereReservationIdIn(Set<Long> reservationIds) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(ReservationToSeatRelation_.RESERVATION).get(AuditedObject_.ID).in(reservationIds);
    }

}
