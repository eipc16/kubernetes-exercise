package com.piisw.cinema_tickets_app.domain.seat.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom_;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SeatSpecification extends AuditedObjectSpecification<Seat> {

    public Specification<Seat> hasScreeningRoomIdAndObjectState(Long screeningRoomId, Set<ObjectState> objectStates) {
        return inScreeningRoom(screeningRoomId).and(hasObjectStateInSet(objectStates));
    }

    private Specification<Seat> inScreeningRoom(Long screeningRoomId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Seat_.SCREENING_ROOM).get(ScreeningRoom_.ID), screeningRoomId);
    }

}
