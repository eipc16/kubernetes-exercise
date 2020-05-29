package com.piisw.cinema_tickets_app.domain.genre.boundary;

import com.piisw.cinema_tickets_app.api.GenreDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.control.GenreService;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAnyRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.SEARCH_TEXT;

@Api(tags = "Genres")
@RestController
@RequestMapping(GenreController.MAIN_PATH)
@AllArgsConstructor
public class GenreController {

    public static final String MAIN_PATH = "/genres";

    private GenreService genreService;
    private GenreMapper genreMapper;

    @ApiOperation(value = "${api.genres.get.value}", notes = "${api.genres.get.notes}")
    @GetMapping
    @HasAnyRole
    public List<GenreDTO> getAllGenres(@RequestParam(name = SEARCH_TEXT, defaultValue = "") String searchText,
                                       @RequestParam(name = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        List<Genre> foundGenres = genreService.getGenresByName(searchText, objectStates);
        return genreMapper.mapToGenreDTOs(foundGenres);
    }
}
