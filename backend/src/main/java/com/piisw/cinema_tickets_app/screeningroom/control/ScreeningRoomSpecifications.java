package com.piisw.cinema_tickets_app.screeningroom.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObjectState;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@UtilityClass
public class ScreeningRoomSpecifications {

    public Specification<ScreeningRoom> hasIdInSetAndObjectStateInSet(Set<Long> ids,
                                                                      Set<AuditedObjectState> objectStates) {
        return hasIdInSet(ids).and(hasObjectStateInSet(objectStates));
    }

    public Specification<ScreeningRoom> hasIdInSet(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(ScreeningRoom_.id).in(ids);
    }

    public Specification<ScreeningRoom> hasObjectStateInSet(Set<AuditedObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

}
