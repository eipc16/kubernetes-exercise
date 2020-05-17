package com.piisw.cinema_tickets_app.screeningroom;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomService;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import com.piisw.cinema_tickets_app.infrastructure.utils.ExceptionUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ScreeningRoomService.class, AuditedObjectSpecification.class, AuditingConfig.class, UserService.class, BCryptPasswordEncoder.class})
public class ScreeningRoomTests {

    @Autowired
    private ScreeningRoomService screeningRoomService;

    @MockBean
    private SeatService seatServiceMock;

    @Captor
    private ArgumentCaptor<ScreeningRoom> screeningRoomCaptor;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldCreateScreeningRoom() {
        ScreeningRoom dummyScreeningRoom = getDummyScreeningRoom();
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(dummyScreeningRoom);
        assertEquals(dummyScreeningRoom.getNumber(), createdScreeningRoom.getNumber());
        assertEquals(dummyScreeningRoom.getRowsNumber(), createdScreeningRoom.getRowsNumber());
        assertEquals(dummyScreeningRoom.getSeatsInRowNumber(), createdScreeningRoom.getSeatsInRowNumber());
        assertNotNull(createdScreeningRoom.getId());
        assertEquals(ObjectState.ACTIVE, createdScreeningRoom.getObjectState());
    }

    private ScreeningRoom getDummyScreeningRoom() {
        return ScreeningRoom.builder()
                .number(12L)
                .rowsNumber(8L)
                .seatsInRowNumber(15L)
                .build();
    }

    @Test
    public void shouldCreateSeatsWhenCreatingScreeningRoom() {
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(getDummyScreeningRoom());
        verify(seatServiceMock, times(1)).createSeatsForScreeningRoom(screeningRoomCaptor.capture());
        assertEquals(createdScreeningRoom.getId(), screeningRoomCaptor.getValue().getId());
    }

    @Test
    public void shouldUpdateScreeningRoom() {
        ScreeningRoom screeningRoom = getDummyScreeningRoom();
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(screeningRoom);
        ScreeningRoom updatedScreeningRoom = createdScreeningRoom.toBuilder()
                .number(24L)
                .rowsNumber(10L)
                .seatsInRowNumber(10L)
                .build();
        ScreeningRoom screeningRoomAfterUpdate = screeningRoomService.updateScreeningRoom(updatedScreeningRoom);
        assertEquals(createdScreeningRoom.getId(), screeningRoomAfterUpdate.getId());
        assertEquals(updatedScreeningRoom.getNumber(), screeningRoomAfterUpdate.getNumber());
        assertEquals(updatedScreeningRoom.getRowsNumber(), screeningRoomAfterUpdate.getRowsNumber());
        assertEquals(updatedScreeningRoom.getSeatsInRowNumber(), screeningRoomAfterUpdate.getSeatsInRowNumber());
        assertEquals(updatedScreeningRoom.getId(), screeningRoomAfterUpdate.getId());
    }

    @Test
    public void shouldUpdateSeatsWhenSeatsNumberInRoomChanges() {
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(getDummyScreeningRoom());
        ScreeningRoom updatedScreeningRoom = createdScreeningRoom.toBuilder()
                .number(24L)
                .rowsNumber(10L)
                .seatsInRowNumber(10L)
                .build();
        screeningRoomService.updateScreeningRoom(updatedScreeningRoom);
        verify(seatServiceMock, times(1)).updateSeatsForScreeningRoom(screeningRoomCaptor.capture());
        assertEquals(createdScreeningRoom.getId(), screeningRoomCaptor.getValue().getId());
    }

    @Test
    public void shouldUpdateScreeningRoomWithoutSeatsWhenOnlyRoomNumberChanged() {
        ScreeningRoom screeningRoom = getDummyScreeningRoom();
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(screeningRoom);
        ScreeningRoom updatedScreeningRoom = createdScreeningRoom.toBuilder()
                .number(24L)
                .build();
        ScreeningRoom screeningRoomAfterUpdate = screeningRoomService.updateScreeningRoom(updatedScreeningRoom);
        verify(seatServiceMock, never()).updateSeatsForScreeningRoom(any());
        assertEquals(updatedScreeningRoom.getNumber(), screeningRoomAfterUpdate.getNumber());
        assertEquals(updatedScreeningRoom.getSeatsInRowNumber(), screeningRoomAfterUpdate.getSeatsInRowNumber());
        assertEquals(updatedScreeningRoom.getRowsNumber(), screeningRoomAfterUpdate.getRowsNumber());
        assertEquals(createdScreeningRoom.getId(), screeningRoomAfterUpdate.getId());
    }

    @Test
    public void shouldThrowWhenTriedToUpdateRemovedScreeningRoom() {
        ScreeningRoom screeningRoom = screeningRoomService.createScreeningRoom(getDummyScreeningRoom());
        screeningRoomService.deleteScreeningRoomsByIds(Set.of(screeningRoom.getId()));
        IllegalArgumentException expectedException = ExceptionUtils.getObjectNotFoundException(ScreeningRoom.class, screeningRoom.getId(), ObjectState.ACTIVE);
        exceptionRule.expect(expectedException.getClass());
        exceptionRule.expectMessage(expectedException.getMessage());
        screeningRoomService.updateScreeningRoom(screeningRoom);
    }

    @Test
    public void shouldRemoveScreeningRooms() {
        List<ScreeningRoom> screeningRooms = getDummyScreeningRooms(10);
        List<ScreeningRoom> createdScreeningRooms = createScreeningRooms(screeningRooms);
        Set<Long> createdScreeningRoomsIds = getScreeningRoomsIds(createdScreeningRooms);
        List<ScreeningRoom> removedScreeningRooms = screeningRoomService.deleteScreeningRoomsByIds(createdScreeningRoomsIds);
        boolean allRemoved = removedScreeningRooms.stream()
                .map(AuditedObject::getObjectState)
                .allMatch(ObjectState.REMOVED::equals);
        assertTrue(allRemoved);
    }

    private List<ScreeningRoom> getDummyScreeningRooms(int count) {
        return Stream.generate(this::getDummyScreeningRoom)
                .limit(count)
                .collect(Collectors.toList());
    }

    private List<ScreeningRoom> createScreeningRooms(List<ScreeningRoom> screeningRooms) {
        return screeningRooms.stream()
                .map(screeningRoomService::createScreeningRoom)
                .collect(Collectors.toList());
    }

    private Set<Long> getScreeningRoomsIds(List<ScreeningRoom> screeningRooms) {
        return screeningRooms.stream()
                .map(AuditedObject::getId)
                .collect(Collectors.toSet());
    }

    @Test
    public void shouldThrowWhenTriedToRemoveNonExistingScreeningRoom() {
        Set<Long> nonExistingIds = Set.of(-1L, -2L, -3L);
        IllegalArgumentException expectedException = ExceptionUtils.getObjectNotFoundException(ScreeningRoom.class, nonExistingIds, ObjectState.ACTIVE);
        exceptionRule.expect(expectedException.getClass());
        exceptionRule.expectMessage(expectedException.getMessage());
        screeningRoomService.deleteScreeningRoomsByIds(nonExistingIds);
    }

}
