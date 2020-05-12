package com.piisw.cinema_tickets_app.domain.seat.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private AuditedObjectSpecification<Seat> specification;

    public List<Seat> getSeatsByIds(Set<Long> ids, ObjectState objectState) {
        return getSeatsByIds(ids, Set.of(objectState));
    }

    public List<Seat> getSeatsByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return seatRepository.findAll(specification.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

}
