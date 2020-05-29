package com.piisw.cinema_tickets_app.domain.seat.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.seat.entity.SeatAvailabilityDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatService {

    private static final long FIRST_SEAT_IN_ROW_NUMBER = 1;
    private static final long FIRST_ROW_IN_SCREENING_ROOM = 1;
    private static final String THERE_ARE_EXISTING_SCREENINGS = "There are existing screenings {0} for screening room {1}";

    private final SeatRepository seatRepository;
    private final SeatSpecification specification;
    private final ScreeningService screeningService;
    private final ReservationService reservationService;

    public List<Seat> getSeatsByIds(Set<Long> ids, ObjectState objectState) {
        return seatRepository.findAll(specification.whereIdInAndObjectStateEquals(ids, objectState));
    }

    public List<Seat> createSeatsForScreeningRoom(ScreeningRoom screeningRoom) {
        List<Seat> seats = buildSeatsForScreeningRoom(screeningRoom);
        return seatRepository.saveAll(seats);
    }

    private List<Seat> buildSeatsForScreeningRoom(ScreeningRoom screeningRoom) {
        return LongStream.rangeClosed(FIRST_ROW_IN_SCREENING_ROOM, screeningRoom.getRowsNumber())
                .boxed()
                .map(rowNumber -> buildRowOfSeats(rowNumber, screeningRoom))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Seat> buildRowOfSeats(Long rowNumber, ScreeningRoom screeningRoom) {
        return LongStream.rangeClosed(FIRST_SEAT_IN_ROW_NUMBER, screeningRoom.getSeatsInRowNumber())
                .boxed()
                .map(seatNumberInRow -> buildNewSeat(seatNumberInRow, rowNumber, screeningRoom))
                .collect(Collectors.toList());
    }

    private Seat buildNewSeat(Long numberInRow, Long rowNumber, ScreeningRoom screeningRoom) {
        return Seat.builder()
                .row(rowNumber)
                .number(numberInRow)
                .screeningRoom(screeningRoom)
                .objectState(ObjectState.ACTIVE)
                .build();
    }

    public List<Seat> updateSeatsForScreeningRoom(ScreeningRoom updatedScreeningRoom) {
        validateIfNoActiveScreeningForUpdatedScreeningRoomExists(updatedScreeningRoom);
        removeSeatsForScreeningRoom(updatedScreeningRoom);
        return createSeatsForScreeningRoom(updatedScreeningRoom);
    }

    public void removeSeatsForScreeningRoom(ScreeningRoom screeningRoom) {
        List<Seat> seats = getSeatsForScreeningRoom(screeningRoom, Set.of(ObjectState.ACTIVE));
        seats.forEach(seat -> seat.setObjectState(ObjectState.REMOVED));
        seatRepository.saveAll(seats);
    }

    private List<Seat> getSeatsForScreeningRoom(ScreeningRoom screeningRoom, Set<ObjectState> objectStates) {
        return seatRepository.findAll(specification.whereScreeningRoomIdEqualsAndObjectStateIn(screeningRoom.getId(), objectStates));
    }

    private void validateIfNoActiveScreeningForUpdatedScreeningRoomExists(ScreeningRoom screeningRoom) {
        Set<Long> existingScreeningsIds = getIdsOfActiveScreeningsForScreeningRoom(screeningRoom);
        if (!existingScreeningsIds.isEmpty()) {
            throw new IllegalArgumentException(MessageFormat.format(THERE_ARE_EXISTING_SCREENINGS, StringUtils.join(existingScreeningsIds), screeningRoom.getId()));
        }
    }

    private Set<Long> getIdsOfActiveScreeningsForScreeningRoom(ScreeningRoom screeningRoom) {
        return screeningService.getScreeningByScreeningRoomId(screeningRoom.getId(), ObjectState.ACTIVE)
                .stream()
                .map(AuditedObject::getId)
                .collect(Collectors.toSet());
    }

    public List<SeatAvailabilityDetails> getSeatsDetailsForScreening(Screening screening, Set<ObjectState> objectStates) {
        List<Reservation> reservations = reservationService.getReservationsForScreening(screening, objectStates);
        List<Seat> reservedSeats = reservationService.getReservedSeats(reservations, objectStates);
        ScreeningRoom screeningRoom = screening.getScreeningRoom();
        List<Seat> allSeatsInRoom = getSeatsForScreeningRoom(screeningRoom, objectStates);
        return allSeatsInRoom.stream()
                .map(seat -> buildSeatAvailabilityDetails(seat, !reservedSeats.contains(seat)))
                .collect(Collectors.toList());
    }

    private SeatAvailabilityDetails buildSeatAvailabilityDetails(Seat seat, boolean isAvailable) {
        return SeatAvailabilityDetails.builder()
                .seat(seat)
                .isAvailable(isAvailable)
                .build();
    }
}
