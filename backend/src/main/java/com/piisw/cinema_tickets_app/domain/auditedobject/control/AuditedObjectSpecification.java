package com.piisw.cinema_tickets_app.domain.auditedobject.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuditedObjectSpecification<T extends AuditedObject> {

    public Specification<T> whereIdAndObjectStateIn(Set<Long> ids, Set<ObjectState> objectStates) {
        return whereIdIn(ids).and(whereObjectStateIn(objectStates));
    }

    public Specification<T> whereIdEqualsAndObjectStateIn(Long id, Set<ObjectState> objectStates) {
        return whereIdEquals(id).and(whereObjectStateIn(objectStates));
    }

    public Specification<T> whereIdInAndObjectStateEquals(Set<Long> ids, ObjectState objectState) {
        return whereIdIn(ids).and(whereObjectStateEquals(objectState));
    }

    public Specification<T> whereIdAndObjectStateEquals(Long id, ObjectState objectState) {
        return whereIdEquals(id).and(whereObjectStateEquals(objectState));
    }

    public Specification<T> whereIdIn(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.id).in(ids);
    }

    public Specification<T> whereIdEquals(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(AuditedObject_.ID), id);
    }

    public Specification<T> whereObjectStateIn(Set<ObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

    public Specification<T> whereObjectStateEquals(ObjectState objectState) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(AuditedObject_.OBJECT_STATE), objectState);
    }

}
