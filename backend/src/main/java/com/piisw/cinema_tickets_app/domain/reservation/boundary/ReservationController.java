package com.piisw.cinema_tickets_app.domain.reservation.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Reservations")
@RestController
@RequestMapping(ReservationController.MAIN_PATH)
public class ReservationController {

    public static final String MAIN_PATH = "/reservation";

    @ApiOperation(value = "${api.reservations.post.value}", notes = "${api.reservations.post.notes}")
    @PostMapping
    @HasAnyRole
    public ResourceDTO createReservation() {
        return null;
    }

}
