package com.piisw.cinema_tickets_app.domain.reservation.boundary;

import com.piisw.cinema_tickets_app.api.ReservationDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectService;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.reservation.control.ReservationService;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.domain.seat.control.SeatService;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.LoggedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS_PATH;

@Api(tags = "Reservations")
@RestController
@RequestMapping(ReservationController.MAIN_PATH)
@AllArgsConstructor
public class ReservationController {

    public static final String MAIN_PATH = "/reservation";

    private ReservationMapper reservationMapper;
    private ReservationService reservationService;
    private SeatService seatService;
    private AuditedObjectService auditedObjectService;

    @ApiOperation(value = "${api.reservations.get.value}", notes = "${api.reservations.get.notes}")
    @GetMapping(IDS_PATH)
    @HasAnyRole
    public List<ReservationDTO> getReservationsByIds(@PathVariable(IDS) Set<Long> ids, @RequestParam(defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        return reservationService.getReservationsByIds(ids, objectStates).stream()
                .map(reservation -> reservationMapper.mapToReservationDTO(reservation, getReservedSeatsIdsForReservation(reservation, objectStates)))
                .collect(Collectors.toList());
    }

    private Set<Long> getReservedSeatsIdsForReservation(Reservation reservation, Set<ObjectState> objectStates) {
        return auditedObjectService.toSetOfIds(reservationService.getReservedSeats(Collections.singletonList(reservation), objectStates));
    }

    @ApiOperation(value = "${api.reservations.post.value}", notes = "${api.reservations.post.notes}")
    @PostMapping
    @HasAnyRole
    public ResourceDTO createReservation(@Validated @RequestBody ReservationDTO reservationDTO, @ApiIgnore @LoggedUser UserInfo userInfo) {
        Reservation reservationToCreate = reservationMapper.mapToReservation(reservationDTO, userInfo);
        List<Seat> seatsToReserve = seatService.getSeatsByIds(reservationDTO.getSeatsIds(), ObjectState.ACTIVE);
        Reservation createdReservation = reservationService.createReservation(reservationToCreate, seatsToReserve, userInfo);
        return reservationMapper.mapToResourceDTO(createdReservation);
    }

    @ApiOperation(value = "${api.reservations.delete.value}", notes = "${api.reservations.delete.notes}")
    @DeleteMapping(IDS_PATH)
    @HasAnyRole
    public List<ResourceDTO> deleteReservationsByIds(@PathVariable(IDS) Set<Long> ids, @ApiIgnore @LoggedUser UserInfo userInfo) {
        List<Reservation> removedReservations = reservationService.removeReservationsByIds(ids, userInfo);
        return removedReservations.stream()
                .map(reservationMapper::mapToResourceDTO)
                .collect(Collectors.toList());
    }
}
