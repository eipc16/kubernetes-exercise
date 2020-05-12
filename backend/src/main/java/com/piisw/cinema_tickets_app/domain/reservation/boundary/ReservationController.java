package com.piisw.cinema_tickets_app.domain.reservation.boundary;

import com.piisw.cinema_tickets_app.api.ReservationDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationMapper;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.LoggedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

@Api(tags = "Reservations")
@RestController
@RequestMapping(ReservationController.MAIN_PATH)
public class ReservationController {

    public static final String MAIN_PATH = "/reservation";

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private ScreeningService screeningService;

    @ApiOperation(value = "${api.reservations.post.value}", notes = "${api.reservations.post.notes}")
    @PostMapping
    @HasAnyRole
    public ResourceDTO createReservation(@Validated @RequestBody ReservationDTO reservationDTO, @ApiIgnore @LoggedUser UserInfo userInfo) {
        Reservation reservation = reservationMapper.mapToReservation(reservationDTO);
        List<Seat> seats = seatService.getSeatsByIds(reservationDTO.getSeatsIds(), ObjectState.ACTIVE);
        Screening screening = screeningService.getScreeningById(reservation.getScreeningId(), ObjectState.ACTIVE);
        reservationService.validateReservation(reservation, screening, seats, userInfo);
        return null;
    }

}
