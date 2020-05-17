package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ScreeningSpecification specification;

    public Screening getScreeningById(Long id, ObjectState objectState) {
        return getScreeningById(id, Set.of(objectState));
    }

    public Screening getScreeningById(Long id, Set<ObjectState> objectStates) {
        return screeningRepository.findOne(specification.whereIdEqualsAndObjectStateIn(id, objectStates))
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(Screening.class, id, objectStates));
    }

    public List<Screening> getScreeningByScreeningRoomId(Long screeningRoomId, ObjectState objectState) {
        return screeningRepository.findAll(specification.whereScreeningRoomIdEqualsAndObjectStateEquals(screeningRoomId, objectState));
    }

    public List<Screening> getScreeningsByMovie(Movie movie, Set<ObjectState> objectStates) {
        return screeningRepository.findAll(specification.whereMovieIdEqualsAndObjectStateIn(movie.getId(), objectStates));
    }

    public Screening createScreening(Screening screening) {
        Screening screeningToCreate = screening.toBuilder()
                .id(null)
                .objectState(ObjectState.ACTIVE)
                .build();
        return screeningRepository.save(screeningToCreate);
    }

    public List<Screening> getScreeningsWhereStartTimeIsBetween(LocalDateTime begin, LocalDateTime end) {
        return screeningRepository.findAll(specification.whereStartTimeBetween(begin, end));
    }

}
