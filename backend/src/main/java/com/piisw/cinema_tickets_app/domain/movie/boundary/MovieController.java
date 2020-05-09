package com.piisw.cinema_tickets_app.domain.movie.boundary;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.*;
import com.piisw.cinema_tickets_app.api.MovieDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieService;
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

@Api(tags = "${api.movies}")
@RestController
@RequestMapping(MovieController.MAIN_PATH)
public class MovieController {

    public static final String MAIN_PATH = "/movies";

    @Autowired
    private MovieService movieService;

    @ApiOperation(value = "${api.movies.get.value}", notes = "${api.movies.get.notes}")
    @GetMapping(IDS_PATH)
    @HasAnyRole
    public List<MovieDTO> getMoviesByIds(@PathVariable(IDS) Set<Long> ids,
                                         @RequestParam(name = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        return Collections.emptyList();
    }

}
