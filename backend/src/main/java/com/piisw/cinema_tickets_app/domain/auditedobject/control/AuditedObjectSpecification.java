package com.piisw.cinema_tickets_app.domain.auditedobject.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AuditedObjectSpecification<T extends AuditedObject> {

    public Specification<T> whereIdAndObjectStateIn(Collection<Long> ids, Collection<ObjectState> objectStates) {
        return whereIdIn(ids).and(whereObjectStateIn(objectStates));
    }

    public Specification<T> whereIdEqualsAndObjectStateIn(Long id, Collection<ObjectState> objectStates) {
        return whereIdEquals(id).and(whereObjectStateIn(objectStates));
    }

    public Specification<T> whereIdInAndObjectStateEquals(Collection<Long> ids, ObjectState objectState) {
        return whereIdIn(ids).and(whereObjectStateEquals(objectState));
    }

    public Specification<T> whereIdAndObjectStateEquals(Long id, ObjectState objectState) {
        return whereIdEquals(id).and(whereObjectStateEquals(objectState));
    }

    public Specification<T> whereIdIn(Collection<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.id).in(ids);
    }

    public Specification<T> whereIdEquals(Long id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(AuditedObject_.ID), id);
    }

    public Specification<T> whereObjectStateIn(Collection<ObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

    public Specification<T> whereObjectStateEquals(ObjectState objectState) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(AuditedObject_.OBJECT_STATE), objectState);
    }

}
