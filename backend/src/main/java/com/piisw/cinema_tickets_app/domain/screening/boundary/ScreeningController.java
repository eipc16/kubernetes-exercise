package com.piisw.cinema_tickets_app.domain.screening.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieService;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomService;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAdminRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.PermitAll;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID_PATH;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;

@Api(tags = "Screenings")
@RestController
@RequestMapping(ScreeningController.MAIN_RESOURCE)
@RequiredArgsConstructor
public class ScreeningController {

    public static final String MAIN_RESOURCE = "/screening";
    public static final String MOVIE_RESOURCE = "/movie";

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final ScreeningRoomService screeningRoomService;
    private final ScreeningMapper screeningMapper;

    @ApiOperation(value = "${api.screenings.get.value}", notes = "${api.screenings.get.notes}")
    @GetMapping(ID_PATH)
    @PermitAll
    public ScreeningDTO getScreeningById(@PathVariable(ID) Long id,
                                         @RequestParam(value = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        Screening screening = screeningService.getScreeningById(id, objectStates);
        return screeningMapper.mapToScreeningDTO(screening);
    }

    @ApiOperation(value = "${api.screenings.for.movie.value}", notes = "${api.screenings.for.movie.get.notes}")
    @GetMapping(MOVIE_RESOURCE + ID_PATH)
    @PermitAll
    public List<ScreeningDTO> getScreeningsForMovie(@PathVariable(ID) Long movieId,
                                                    @RequestParam(value = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        Movie movie = movieService.getMovieById(movieId, objectStates);
        List<Screening> screenings = screeningService.getScreeningsByMovie(movie, objectStates);
        return screenings.stream()
                .map(screeningMapper::mapToScreeningDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "${api.screenings.post.value}", notes = "${api.screenings.post.notes}")
    @PostMapping
    @HasAdminRole
    public ResourceDTO createScreeningForMovie(@Validated @RequestBody ScreeningDTO screeningDTO) {
        Movie movie = movieService.getMovieById(screeningDTO.getMovieId(), ObjectState.ACTIVE);
        ScreeningRoom screeningRoom = screeningRoomService.getScreeningRoomById(screeningDTO.getScreeningRoomId(), ObjectState.ACTIVE);
        Screening screening = screeningMapper.mapToScreening(screeningDTO, movie, screeningRoom);
        Screening createdScreening = screeningService.createScreening(screening);
        return screeningMapper.mapToResourceDTO(createdScreening);
    }

}
