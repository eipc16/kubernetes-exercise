package com.piisw.cinema_tickets_app.domain.seat.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SeatSpecification extends AuditedObjectSpecification<Seat> {

    public Specification<Seat> whereScreeningRoomIdEqualsAndObjectStateEquals(Long screeningRoomId, ObjectState objectState) {
        return whereScreeningRoomIdEquals(screeningRoomId).and(whereObjectStateEquals(objectState));
    }

    public Specification<Seat> whereScreeningRoomIdEqualsAndObjectStateIn(Long screeningRoomId, Set<ObjectState> objectStates) {
        return whereScreeningRoomIdEquals(screeningRoomId).and(whereObjectStateIn(objectStates));
    }

    private Specification<Seat> whereScreeningRoomIdEquals(Long screeningRoomId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Seat_.SCREENING_ROOM).get(AuditedObject_.ID), screeningRoomId);
    }

}
