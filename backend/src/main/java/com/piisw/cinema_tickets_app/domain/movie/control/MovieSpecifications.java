package com.piisw.cinema_tickets_app.domain.movie.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie_;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@UtilityClass
public class MovieSpecifications {

    public Specification<Movie> hasIdInSetAndObjectStateInSet(Set<Long> ids,
                                                              Set<ObjectState> objectStates) {
        return hasIdInSet(ids).and(hasObjectStateInSet(objectStates));
    }

    public Specification<Movie> hasIdInSet(Set<Long> ids) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(Movie_.id).in(ids);
    }

    public Specification<Movie> hasObjectStateInSet(Set<ObjectState> objectStates) {
        return (root, criteriaQuery, criteriaBuilder) -> root.get(AuditedObject_.objectState).in(objectStates);
    }

}
