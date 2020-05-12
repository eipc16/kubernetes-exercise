package com.piisw.cinema_tickets_app.domain.screeningroom.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScreeningRoomService {

    private static final String SCREENING_ROOM_NOT_FOUND = "Screening room with id {0} doesn''t exists";

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private AuditedObjectSpecification<ScreeningRoom> specification;

    @Autowired
    private SeatService seatService;

    public List<ScreeningRoom> getAllScreeningRoomsByIdsAndObjectStates(Set<Long> ids, Set<ObjectState> objectStates) {
        return screeningRoomRepository.findAll(specification.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

    public ScreeningRoom createScreeningRoom(ScreeningRoom screeningRoom) {
        ScreeningRoom newScreeningRoom = screeningRoom.toBuilder()
                .id(null)
                .objectState(ObjectState.ACTIVE)
                .build();
        ScreeningRoom createdScreeningRoom = screeningRoomRepository.save(newScreeningRoom);
        seatService.createSeatsForScreeningRoom(createdScreeningRoom);
        return createdScreeningRoom;
    }

    public ScreeningRoom updateScreeningRoom(ScreeningRoom screeningRoom) {
        Objects.requireNonNull(screeningRoom.getId());
        ScreeningRoom existingScreeningRoom = getExistingScreeningRoomById(screeningRoom.getId());
        updateSeatsForScreeningRoomIfNecessary(existingScreeningRoom, screeningRoom);
        ScreeningRoom screeningRoomToUpdate = existingScreeningRoom.toBuilder()
                .number(screeningRoom.getNumber())
                .rowsNumber(screeningRoom.getRowsNumber())
                .seatsInRowNumber(screeningRoom.getSeatsInRowNumber())
                .build();
        ScreeningRoom updatedScreeningRoom = screeningRoomRepository.save(screeningRoomToUpdate);
        return updatedScreeningRoom;
    }

    private ScreeningRoom getExistingScreeningRoomById(Long id) {
        return screeningRoomRepository.findByIdAndObjectState(id, ObjectState.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(SCREENING_ROOM_NOT_FOUND, id)));
    }

    private void updateSeatsForScreeningRoomIfNecessary(ScreeningRoom oldScreeningROom, ScreeningRoom updatedScreeningRoom) {
        if (shouldUpdateScreeningRoomSeats(oldScreeningROom, updatedScreeningRoom)) {
            seatService.updateSeatsForScreeningRoom(updatedScreeningRoom);
        }
    }

    private boolean shouldUpdateScreeningRoomSeats(ScreeningRoom existingScreeningRoom, ScreeningRoom updatedScreeningRoom) {
        return !existingScreeningRoom.getRowsNumber().equals(updatedScreeningRoom.getRowsNumber())
                || !existingScreeningRoom.getSeatsInRowNumber().equals(updatedScreeningRoom.getSeatsInRowNumber());
    }

    public List<ScreeningRoom> deleteScreeningRoomsByIds(Set<Long> ids) {
        List<ScreeningRoom> screeningRoomsToRemove = screeningRoomRepository
                .findAll(specification.hasIdInSetAndObjectStateInSet(ids, ObjectState.existingStates()));
        validateIfAllScreeningRoomsToRemoveExists(ids, screeningRoomsToRemove);
        screeningRoomsToRemove.forEach(screeningRoom -> screeningRoom.setObjectState(ObjectState.REMOVED));
        List<ScreeningRoom> removedScreeningRooms = screeningRoomRepository.saveAll(screeningRoomsToRemove);
        removedScreeningRooms.forEach(seatService::removeSeatsForScreeningRoom);
        return removedScreeningRooms;
    }

    private void validateIfAllScreeningRoomsToRemoveExists(Set<Long> requestedToRemove, List<ScreeningRoom> found) {
        Set<Long> nonExistingIds = getIdsOfNonExistingScreenRooms(requestedToRemove, found);
        if (!nonExistingIds.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(SCREENING_ROOM_NOT_FOUND, StringUtils.join(nonExistingIds)));
        }
    }

    private Set<Long> getIdsOfNonExistingScreenRooms(Set<Long> requestedToRemove, List<ScreeningRoom> foundScreeningRooms) {
        return foundScreeningRooms.stream()
                .map(ScreeningRoom::getId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), foundIds -> Sets.difference(requestedToRemove, foundIds)));
    }

}
