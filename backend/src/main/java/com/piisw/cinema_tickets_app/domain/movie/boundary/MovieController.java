package com.piisw.cinema_tickets_app.domain.movie.boundary;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.*;

import com.piisw.cinema_tickets_app.api.MovieDTO;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieService;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAdminRole;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "Movies")
@RestController
@RequestMapping(MovieController.MAIN_PATH)
public class MovieController {

    public static final String MAIN_PATH = "/movies";

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMapper movieMapper;

    @ApiOperation(value = "${api.movies.get.value}", notes = "${api.movies.get.notes}")
    @GetMapping(IDS_PATH)
    @HasAnyRole
    public List<MovieDTO> getMoviesByIds(@PathVariable(IDS) Set<Long> ids,
                                         @RequestParam(name = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        List<Movie> foundMovies = movieService.getMoviesByIds(ids, objectStates);
        return movieMapper.mapToMovieDTOs(foundMovies);
    }

    @ApiOperation(value = "${api.movies.post.value}", notes = "${api.movies.post.notes}")
    @PostMapping(IDS_PATH)
    @HasAdminRole
    public List<ResourceDTO> createMovies(@PathVariable(IDS) Set<String> imdbIds) {
        List<Movie> createdMovies = movieService.createMovies(imdbIds);
        return createdMovies.stream()
                .map(movieMapper::mapToResourceDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "${api.movies.delete.value}", notes = "${api.movies.delete.notes}")
    @DeleteMapping(IDS_PATH)
    @HasAdminRole
    public List<ResourceDTO> deleteMoviesById(@PathVariable(IDS) Set<Long> ids) {
        List<Movie> removedMovies = movieService.deleteMoviesByIds(ids);
        return removedMovies.stream()
                .map(movieMapper::mapToResourceDTO)
                .collect(Collectors.toList());
    }

}
