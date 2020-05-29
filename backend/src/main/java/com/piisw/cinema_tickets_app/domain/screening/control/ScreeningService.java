package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.movie.entity.MovieScreeningSearchParams;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningSpecification specification;

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

    public List<Screening> getScreeingsBySearchParams(MovieScreeningSearchParams searchParams) {
        List<Specification<Screening>> specifications = new ArrayList<>();
        specifications.add(specification.whereStartTimeBetween(searchParams.getBeginDateTime(), searchParams.getEndDateTime()));
        if (searchParams.getGenres() != null) {
            specifications.add(specification.whereMovieGenreIn(searchParams.getGenres()));
        }
        if (searchParams.getSearchText() != null) {
            specifications.add(specification.whereMovieTitleLike(searchParams.getSearchText()));
        }
        return screeningRepository.findAll(specification.mergeSpecifications(specifications));
    }
}
