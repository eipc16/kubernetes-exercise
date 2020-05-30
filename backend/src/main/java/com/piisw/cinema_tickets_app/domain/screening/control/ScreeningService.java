package com.piisw.cinema_tickets_app.domain.screening.control;

import com.google.common.base.Strings;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieScreeningSearchParams;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreeningService {

    private static final String SCREENING_ROOM_IN_USE = "There are already defined screenings with ids {0} in screening room with id {1} between {2} and {3}";
    private static final String DURATION_OF_SCREENING_SHORTER_THAN_MOVIE = "Duration of screening cannot be shorter than movie duration ({0})";
    private static final String NOT_AVAILABLE = "N/A";
    private static final String MIN_SUFFIX = " min";

    private final ScreeningRepository screeningRepository;
    private final ScreeningSpecification specification;
    private final AuditedObjectService auditedObjectService;

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
        validateScreeningOnCreate(screening);
        Screening screeningToCreate = screening.toBuilder()
                .id(null)
                .objectState(ObjectState.ACTIVE)
                .build();
        return screeningRepository.save(screeningToCreate);
    }

    private void validateScreeningOnCreate(Screening screening) {
        validateIfStartTimeLesserThanEndTime(screening);
        validateScreeningDuration(screening);
        validateIfScreeningRoomIsAvailable(screening);
    }

    private void validateIfStartTimeLesserThanEndTime(Screening screening) {
        if (screening.getEndTime().compareTo(screening.getStartTime()) < 0) {
            throw new IllegalArgumentException("Start time must be lesser than end time");
        }
    }

    private void validateScreeningDuration(Screening screening) {
        String runTime = screening.getMovie().getRunTime();
        if (!runTime.equals(NOT_AVAILABLE)) {
            Duration screeningDuration = Duration.between(screening.getStartTime(), screening.getEndTime());
            Duration movieDuration = Duration.ofMinutes(Long.parseLong(StringUtils.removeEnd(runTime, MIN_SUFFIX)));
            if (screeningDuration.compareTo(movieDuration) < 0) {
                throw new IllegalArgumentException(MessageFormat.format(DURATION_OF_SCREENING_SHORTER_THAN_MOVIE, runTime));
            }
        }
    }

    private void validateIfScreeningRoomIsAvailable(Screening screening) {
        Set<Long> overlappingScreeningsIds = getIdsOfOverlappingScreenings(screening);
        if (!overlappingScreeningsIds.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(SCREENING_ROOM_IN_USE,
                    StringUtils.join(overlappingScreeningsIds), screening.getScreeningRoom().getId().toString(),
                    screening.getStartTime(), screening.getEndTime()));
        }
    }

    private Set<Long> getIdsOfOverlappingScreenings(Screening screening) {
        List<Screening> overlappingScreenings = screeningRepository.findAll(specification.whereOverlapsWith(screening));
        return auditedObjectService.toSetOfIds(overlappingScreenings);
    }

    public List<Screening> getScreeningsBySearchParams(MovieScreeningSearchParams searchParams) {
        List<Specification<Screening>> specifications = new ArrayList<>();
        specifications.add(specification.whereStartTimeBetween(searchParams.getBeginDateTime(), searchParams.getEndDateTime()));
        if (CollectionUtils.isEmpty(searchParams.getGenres())) {
            specifications.add(specification.whereMovieGenreIn(searchParams.getGenres()));
        }
        if (!Strings.isNullOrEmpty(searchParams.getSearchText())) {
            specifications.add(specification.whereMovieTitleLike(searchParams.getSearchText()));
        }
        return screeningRepository.findAll(specification.mergeSpecifications(specifications));
    }
}
