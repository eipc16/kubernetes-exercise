package com.piisw.cinema_tickets_app.domain.screeningroom.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@UtilityClass
public class ScreeningRoomSpecifications {

    public Specification<ScreeningRoom> hasIdInSetAndObjectStateInSet(Set<Long> ids,
                                                                      Set<ObjectState> objectStates) {
        return hasIdInSet(ids).and(hasObjectStateInSet(objectStates));
    }

    public Specification<ScreeningRoom> hasIdInSet(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(ScreeningRoom_.id).in(ids);
    }

    public Specification<ScreeningRoom> hasObjectStateInSet(Set<ObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

}
