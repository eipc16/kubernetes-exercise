package com.piisw.cinema_tickets_app.domain.screening.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@Service
public class ScreeningService {

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ScreeningSpecification specification;

    private static final String SCREENING_NOT_FOUND = "Screening with id {0} and state {1} not found";

    public List<Screening> getScreeningsByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return screeningRepository.findAll(specification.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

    public Screening getScreeningById(Long id, ObjectState objectState) {
        return getScreeningById(id, Set.of(objectState));
    }

    public Screening getScreeningById(Long id, Set<ObjectState> objectStates) {
        return screeningRepository.findOne(specification.hasIdInSetAndObjectStateInSet(Set.of(id), objectStates))
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(SCREENING_NOT_FOUND, id, StringUtils.join(objectStates, " or "))));
    }

    public List<Screening> getScreeningByScreeningRoomId(Long screeningRoomId, Set<ObjectState> objectStates) {
        return screeningRepository.findAll(specification.hasScreeningRoomIdAndObjectStateInSet(screeningRoomId, objectStates));
    }

}
