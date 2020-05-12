package com.piisw.cinema_tickets_app.domain.seat.boundary;

import com.piisw.cinema_tickets_app.api.SeatDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.seat.entity.Seat;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID_PATH;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;

@Api(tags = "Seats")
@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private ScreeningService screeningService;

    @ApiOperation(value = "${api.seats.get.value}", notes = "${api.seats.get.notes}")
    @GetMapping("/screening" + ID_PATH)
    @HasAnyRole
    public List<SeatDTO> getSeatsForScreening(@PathVariable(ID) Long screeningId,
                                              @RequestParam(value = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        Screening screening = screeningService.getScreeningById(screeningId, objectStates);
        return Collections.emptyList();
    }

}
