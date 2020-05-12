package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ScreeningSpecification extends AuditedObjectSpecification<Screening> {

    public Specification<Screening> hasScreeningRoomIdAndObjectStateInSet(Long screeningRoomId, Set<ObjectState> objectStates) {
        return hasScreeningRoomId(screeningRoomId).and(hasObjectStateInSet(objectStates));
    }

    private Specification<Screening> hasScreeningRoomId(Long screeningRoomId) {
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Screening_.SCREENING_ROOM).get(AuditedObject_.ID), screeningRoomId));
    }

}
