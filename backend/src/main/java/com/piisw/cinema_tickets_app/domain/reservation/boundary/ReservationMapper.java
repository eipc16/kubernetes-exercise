package com.piisw.cinema_tickets_app.domain.reservation.boundary;

import com.piisw.cinema_tickets_app.api.ReservationDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.domain.reservation.entity.Reservation;
import com.piisw.cinema_tickets_app.infrastructure.security.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID_PATH;

@Component
public class ReservationMapper {

    public Reservation mapToReservation(ReservationDTO reservationDTO, UserInfo userInfo) {
        return Reservation.builder()
                .id(reservationDTO.getId())
                .screeningId(reservationDTO.getScreeningId())
                .reservedByUser(Optional.ofNullable(reservationDTO.getReservedByUser()).orElse(userInfo.getId()))
                .objectState(reservationDTO.getObjectState())
                .build();
    }

    public ReservationDTO mapToReservationDTO(Reservation reservation, Set<Long> seatsIds) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .reservedByUser(reservation.getReservedByUser())
                .screeningId(reservation.getScreeningId())
                .seatsIds(seatsIds)
                .objectState(reservation.getObjectState())
                .build();
    }

    public ResourceDTO mapToResourceDTO(Reservation reservation) {
        return ResourceDTO.builder()
                .id(reservation.getId())
                .identifier(reservation.getId().toString())
                .uri(buildReservationUri(reservation))
                .build();
    }

    private URI buildReservationUri(Reservation reservation) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ReservationController.MAIN_PATH)
                .path(ID_PATH)
                .buildAndExpand(reservation.getId())
                .toUri();
    }

}
