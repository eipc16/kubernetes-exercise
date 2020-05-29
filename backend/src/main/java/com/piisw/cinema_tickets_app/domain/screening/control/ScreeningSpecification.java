package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie_;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class ScreeningSpecification extends AuditedObjectSpecification<Screening> {

    public Specification<Screening> whereScreeningRoomIdEqualsAndObjectStateEquals(Long screeningRoomId, ObjectState objectState) {
        return whereScreeningRoomIdEquals(screeningRoomId).and(whereObjectStateEquals(objectState));
    }

    private Specification<Screening> whereScreeningRoomIdEquals(Long screeningRoomId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Screening_.SCREENING_ROOM).get(AuditedObject_.ID), screeningRoomId);
    }

    public Specification<Screening> whereMovieIdEqualsAndObjectStateIn(Long movieId, Set<ObjectState> objectStates) {
        return whereMovieIdEquals(movieId).and(whereObjectStateIn(objectStates));
    }

    public Specification<Screening> whereMovieIdEquals(Long movieId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Screening_.MOVIE).get(AuditedObject_.ID), movieId);
    }

    public Specification<Screening> whereMovieTitleLike(String searchText) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(Screening_.MOVIE).get(Movie_.TITLE), searchText);
    }

    public Specification<Screening> whereMovieGenreIn(List<String> genres) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            // TODO: Add filtering by genre name
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<Screening> distinct() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.select(root.get(Screening_.MOVIE).get(Movie_.IMDB_ID)).distinct(true);
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<Screening> whereStartTimeBetween(LocalDateTime begin, LocalDateTime end) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(Screening_.START_TIME), begin, end);
    }

    public Specification<Screening> mergeSpecifications(@NotEmpty List<Specification<Screening>> specifications) {
        if (specifications.size() == 1) {
            return specifications.get(0);
        }
        return specifications.stream()
                .filter(Objects::nonNull)
                .reduce(specifications.get(0), Specification::and);
    }
}
