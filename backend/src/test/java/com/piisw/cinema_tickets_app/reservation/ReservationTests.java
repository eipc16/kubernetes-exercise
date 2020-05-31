package com.piisw.cinema_tickets_app.reservation;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieRepository;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationService;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationSpecification;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationToRelationSpecification;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationToSeatRelationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.reservation.entity.ReservationState;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningRepository;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningSpecification;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomRepository;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatSpecification;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.domain.seat.entity.SeatAvailabilityDetails;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.domain.user.entity.User;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import com.piisw.cinema_tickets_app.screening.ScreeningTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({AuthenticationService.class, UserService.class, BCryptPasswordEncoder.class, AuditingConfig.class,
        ReservationService.class, SeatService.class, ReservationSpecification.class, ReservationToSeatRelationService.class,
        ReservationToRelationSpecification.class, AuditedObjectService.class, ScreeningService.class, ScreeningSpecification.class,
        SeatSpecification.class})
public class ReservationTests {

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserService userService;

    @Autowired
    private SeatService seatService;

    @Test
    public void shouldCreteReservation() {
        ScreeningRoom screeningRoom = screeningRoomRepository.save(ScreeningTests.getDummyScreeningRoom());
        Movie movie = movieRepository.save(ScreeningTests.getDummyMovie());
        Screening screening = screeningRepository.save(ScreeningTests.getDummyScreening(screeningRoom, movie)
                .toBuilder()
                .startTime(LocalDateTime.now().plusHours(2))
                .endTime(LocalDateTime.now().plusHours(5))
                .build());
        User user = userService.registerUser(getDummyUser());
        Reservation dummyReservation = getDummyReservation(screening, user);
        List<SeatAvailabilityDetails> seats = seatService.getSeatsDetailsForScreening(screening, Set.of(ObjectState.ACTIVE), user.getId());
        Set<Seat> seatsToReserve = seats.stream()
                .map(SeatAvailabilityDetails::getSeat)
                .limit(3)
                .collect(Collectors.toSet());
        Set<Seat> seatsToBeStillAvailable = seats.stream()
                .filter(seat -> !seatsToReserve.contains(seat))
                .map(SeatAvailabilityDetails::getSeat)
                .collect(Collectors.toSet());
        reservationService.createReservation(dummyReservation, seatsToReserve, UserInfo.fromUser(user));
        List<SeatAvailabilityDetails> seatsAfterReservation = seatService.getSeatsDetailsForScreening(screening, Set.of(ObjectState.ACTIVE), user.getId());
        Map<Boolean, Set<Seat>> seatsByIsAvailable = seatsAfterReservation.stream()
                .collect(Collectors.partitioningBy(seat -> ReservationState.AVAILABLE == seat.getReservationState(),
                        Collectors.mapping(SeatAvailabilityDetails::getSeat, Collectors.toSet())));
        assertEquals(seatsToReserve, seatsByIsAvailable.get(false));
        assertEquals(seatsToBeStillAvailable, seatsByIsAvailable.get(true));
    }

    private Reservation getDummyReservation(Screening screening, User user) {
        return Reservation.builder()
                .screeningId(screening.getId())
                .reservedByUser(user.getId())
                .objectState(ObjectState.ACTIVE)
                .build();
    }

    private User getDummyUser() {
        return User.builder()
                .name("Johnny")
                .surname("Bravo")
                .username("Johnny123")
                .email("johnny.bravo@examle.com")
                .password("weakPassword123")
                .phoneNumber("123456789")
                .objectState(ObjectState.ACTIVE)
                .userRole(UserRole.ROLE_USER)
                .build();
    }
}
