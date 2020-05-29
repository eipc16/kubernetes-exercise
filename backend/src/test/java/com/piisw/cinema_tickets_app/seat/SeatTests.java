package com.piisw.cinema_tickets_app.seat;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieRepository;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationService;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationSpecification;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationToRelationSpecification;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationToSeatRelationService;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningRepository;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningSpecification;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomService;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatSpecification;
import com.piisw.cinema_tickets_app.domain.seat.entity.SeatAvailabilityDetails;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import com.piisw.cinema_tickets_app.screening.ScreeningTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({AuthenticationService.class, UserService.class, BCryptPasswordEncoder.class, AuditingConfig.class, SeatService.class,
        ScreeningRoomService.class, ScreeningService.class, SeatSpecification.class, ScreeningSpecification.class,
        ReservationService.class, ReservationSpecification.class, ReservationToSeatRelationService.class, ReservationToRelationSpecification.class,
        AuditedObjectService.class, AuditedObjectSpecification.class})
public class SeatTests {

    @Autowired
    private ScreeningRoomService screeningRoomService;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRepository screeningRepository;

    @Autowired
    private SeatService seatService;

    @Test
    public void shouldReturnAllSeatsAsAvailableForScreeningWithNoReservation() {
        ScreeningRoom screeningRoom = screeningRoomService.createScreeningRoom(ScreeningTests.getDummyScreeningRoom());
        Movie movie = movieRepository.save(ScreeningTests.getDummyMovie());
        Screening screening = screeningRepository.save(ScreeningTests.getDummyScreening(screeningRoom, movie));
        List<SeatAvailabilityDetails> seatsDetailsForScreening = seatService.getSeatsDetailsForScreening(screening, Set.of(ObjectState.ACTIVE));
        boolean allSeatsAreAvailable = seatsDetailsForScreening.stream()
                .map(SeatAvailabilityDetails::isAvailable)
                .allMatch(Boolean.TRUE::equals);
        assertTrue(allSeatsAreAvailable);
    }

}
