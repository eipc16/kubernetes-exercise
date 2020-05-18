package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ReservationSpecification extends AuditedObjectSpecification<Reservation> {

    public Specification<Reservation> whereScreeningIdEqualsAndObjectStateIn(Long screeningId, Set<ObjectState> objectStates) {
        return whereScreeningIdEquals(screeningId).and(whereObjectStateIn(objectStates));
    }

    public Specification<Reservation> whereScreeningIdEquals(Long screeningId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Reservation_.SCREENING_ID), screeningId);
    }

}
