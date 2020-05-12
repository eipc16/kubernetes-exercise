package com.piisw.cinema_tickets_app.domain.auditedobject.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuditedObjectSpecification<T extends AuditedObject> {

    public Specification<T> hasIdInSetAndObjectStateInSet(Set<Long> ids,
                                                          Set<ObjectState> objectStates) {
        return hasIdInSet(ids).and(hasObjectStateInSet(objectStates));
    }

    public Specification<T> hasIdInSet(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.id).in(ids);
    }

    public Specification<T> hasObjectStateInSet(Set<ObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

}
