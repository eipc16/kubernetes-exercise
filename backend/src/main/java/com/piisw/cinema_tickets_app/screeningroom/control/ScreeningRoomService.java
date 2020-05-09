package com.piisw.cinema_tickets_app.screeningroom.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObjectState;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ScreeningRoomService {

    private static final String SCREENING_ROOM_NOT_FOUND = "Screening room with id {0} doesn''t exists";

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    public List<ScreeningRoom> getAllScreeningRoomsByIdsAndObjectStates(Set<Long> ids, Set<AuditedObjectState> objectStates) {
        return screeningRoomRepository.findAll(ScreeningRoomSpecifications.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

    public ScreeningRoom createScreeningRoom(ScreeningRoom screeningRoom) {
        ScreeningRoom newScreeningRoom = screeningRoom.toBuilder()
                .id(null)
                .objectState(AuditedObjectState.ACTIVE)
                .build();
        return screeningRoomRepository.save(newScreeningRoom);
    }

    public ScreeningRoom updateScreeningRoom(ScreeningRoom screeningRoom) {
        Objects.requireNonNull(screeningRoom.getId());
        ScreeningRoom existingScreeningRoom = getExistingScreeningRoomById(screeningRoom.getId());
        ScreeningRoom updatedScreeningRoom = existingScreeningRoom.toBuilder()
                .number(screeningRoom.getNumber())
                .numberOfSeats(screeningRoom.getNumberOfSeats())
                .build();
        return screeningRoomRepository.save(updatedScreeningRoom);
    }

    private ScreeningRoom getExistingScreeningRoomById(Long id) {
        return screeningRoomRepository.findByIdAndObjectState(id, AuditedObjectState.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(SCREENING_ROOM_NOT_FOUND, id)));
    }

    public List<ScreeningRoom> deleteScreeningRoomsByIds(Set<Long> ids) {
        List<ScreeningRoom> screeningRoomsToRemove = screeningRoomRepository
                .findAll(ScreeningRoomSpecifications.hasIdInSetAndObjectStateInSet(ids, AuditedObjectState.existingStates()));
        validateIfAllScreeningRoomsToRemoveExists(ids, screeningRoomsToRemove);
        screeningRoomsToRemove.stream()
                .forEach(screeningRoom -> screeningRoom.setObjectState(AuditedObjectState.REMOVED));
        return screeningRoomRepository.saveAll(screeningRoomsToRemove);
    }

    private void validateIfAllScreeningRoomsToRemoveExists(Set<Long> requestedToRemove, List<ScreeningRoom> found) {
        Set<Long> nonExistingIds = getIdsOfNonExistingScreenRooms(requestedToRemove, found);
        if (!nonExistingIds.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(SCREENING_ROOM_NOT_FOUND, StringUtils.join(nonExistingIds)));
        }
    }

    private Set<Long> getIdsOfNonExistingScreenRooms(Set<Long> requestedToRemove, List<ScreeningRoom> found) {
        return found.stream()
                .map(ScreeningRoom::getId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), foundIds -> Sets.difference(requestedToRemove, foundIds)));
    }

}
