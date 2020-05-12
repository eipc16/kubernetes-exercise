package com.piisw.cinema_tickets_app.domain.reservation.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationToSeatRelation_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationToRelationSpecification {

    public Specification<ReservationToSeatRelation> hasSeatIdInSet(List<Long> seatsIds) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(ReservationToSeatRelation_.SEAT).get(AuditedObject_.ID).in(seatsIds);
    }

}
