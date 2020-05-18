package com.piisw.cinema_tickets_app.domain.screeningroom.control;

import com.google.common.collect.Sets;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScreeningRoomService {

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private AuditedObjectSpecification<ScreeningRoom> specification;

    @Autowired
    private SeatService seatService;

    public ScreeningRoom getScreeningRoomById(Long id, ObjectState objectState) {
        return screeningRoomRepository.findOne(specification.whereIdAndObjectStateEquals(id, objectState))
                .orElseThrow(() -> ExceptionUtils.getObjectNotFoundException(ScreeningRoom.class, id, objectState));
    }

    public List<ScreeningRoom> getScreeningRoomsByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return screeningRoomRepository.findAll(specification.whereIdAndObjectStateIn(ids, objectStates));
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
        ScreeningRoom existingScreeningRoom = getScreeningRoomById(screeningRoom.getId(), ObjectState.ACTIVE);
        updateSeatsForScreeningRoomIfNecessary(existingScreeningRoom, screeningRoom);
        ScreeningRoom screeningRoomToUpdate = existingScreeningRoom.toBuilder()
                .number(screeningRoom.getNumber())
                .rowsNumber(screeningRoom.getRowsNumber())
                .seatsInRowNumber(screeningRoom.getSeatsInRowNumber())
                .build();
        return screeningRoomRepository.save(screeningRoomToUpdate);
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
                .findAll(specification.whereIdInAndObjectStateEquals(ids, ObjectState.ACTIVE));
        validateIfAllScreeningRoomsToRemoveExists(ids, screeningRoomsToRemove);
        screeningRoomsToRemove.forEach(seatService::removeSeatsForScreeningRoom);
        screeningRoomsToRemove.forEach(screeningRoom -> screeningRoom.setObjectState(ObjectState.REMOVED));
        return screeningRoomRepository.saveAll(screeningRoomsToRemove);
    }

    private void validateIfAllScreeningRoomsToRemoveExists(Set<Long> requestedToRemove, List<ScreeningRoom> found) {
        Set<Long> nonExistingIds = getIdsOfNonExistingScreenRooms(requestedToRemove, found);
        if (!nonExistingIds.isEmpty()) {
            throw ExceptionUtils.getObjectNotFoundException(ScreeningRoom.class, nonExistingIds, ObjectState.ACTIVE);
        }
    }

    private Set<Long> getIdsOfNonExistingScreenRooms(Set<Long> requestedToRemove, List<ScreeningRoom> foundScreeningRooms) {
        return foundScreeningRooms.stream()
                .map(ScreeningRoom::getId)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), foundIds -> Sets.difference(requestedToRemove, foundIds)));
    }

}
