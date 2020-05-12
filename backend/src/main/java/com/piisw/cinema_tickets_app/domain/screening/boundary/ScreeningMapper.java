package com.piisw.cinema_tickets_app.domain.screening.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS_PATH;

@Component
public class ScreeningMapper {

    public ScreeningDTO mapToScreeningDTO(Screening screening) {
        return ScreeningDTO.builder()
                .screeningId(screening.getId())
                .movieId(screening.getMovie().getId())
                .screeningRoomId(screening.getScreeningRoom().getId())
                .startTime(screening.getStartTime())
                .objectState(screening.getObjectState())
                .build();
    }

    public Screening mapToScreening(ScreeningDTO screeningDTO, Movie movie, ScreeningRoom screeningRoom) {
        return Screening.builder()
                .movie(movie)
                .screeningRoom(screeningRoom)
                .startTime(screeningDTO.getStartTime())
                .objectState(screeningRoom.getObjectState())
                .build();
    }

    public ResourceDTO mapToResourceDTO(Screening screening) {
        return ResourceDTO.builder()
                .id(screening.getId())
                .identifier(screening.getId().toString())
                .uri(buildResourceDTO(screening.getId()))
                .build();
    }

    private URI buildResourceDTO(Long id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ScreeningController.MAIN_PATH)
                .path(IDS_PATH)
                .buildAndExpand(id)
                .toUri();
    }

}
