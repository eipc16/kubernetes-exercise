package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject_;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre_;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieToGenreRelation_;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie_;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.List;
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
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(Screening_.MOVIE).get(Movie_.TITLE), applyWildcards(searchText));
    }

    private String applyWildcards(String text) {
        return '%' + text + '%';
    }

    public Specification<Screening> whereMovieGenreIn(List<String> genres) {
        return (root, criteriaQuery, criteriaBuilder) ->
                root.join(Screening_.movie)
                        .join(Movie_.movieToGenreRelations)
                        .join(MovieToGenreRelation_.genre)
                        .get(Genre_.name)
                        .in(genres);
    }

    public Specification<Screening> whereStartTimeBetween(LocalDateTime begin, LocalDateTime end) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.between(root.get(Screening_.START_TIME), begin, end);
    }

    public Specification<Screening> whereOverlapsWith(Screening screening) {
        Long screeningRoomId = screening.getScreeningRoom().getId();
        return whereScreeningRoomIdEquals(screeningRoomId).and(whereScreeningTimeOverlaps(screening.getStartTime(), screening.getEndTime()));
    }

    private Specification<Screening> whereScreeningTimeOverlaps(LocalDateTime startTime, LocalDateTime endTime) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate beginsBeforeOrAtEndTime = criteriaBuilder.lessThanOrEqualTo(root.get(Screening_.startTime), endTime);
            Predicate endsAfterOrAtStartTime = criteriaBuilder.greaterThanOrEqualTo(root.get(Screening_.endTime), startTime);
            return criteriaBuilder.and(beginsBeforeOrAtEndTime, endsAfterOrAtStartTime);
        };
    }

}
