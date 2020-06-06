package com.piisw.cinema_tickets_app.domain.screening.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningDTO;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.boundary.ScreeningRoomMapper;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.IDS_PATH;

@Component
@RequiredArgsConstructor
public class ScreeningMapper {

    private final ScreeningRoomMapper screeningRoomMapper;

    public ScreeningDTO mapToScreeningDTO(Screening screening) {
        return ScreeningDTO.builder()
                .screeningId(screening.getId())
                .movieId(screening.getMovie().getId())
                .screeningRoomId(screening.getScreeningRoom().getId())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .price(screening.getPrice().toString())
                .objectState(screening.getObjectState())
                .screeningRoom(screeningRoomMapper.mapToScreeningRoomDTO(screening.getScreeningRoom()))
                .build();
    }

    public Screening mapToScreening(ScreeningDTO screeningDTO, Movie movie, ScreeningRoom screeningRoom) {
        return Screening.builder()
                .movie(movie)
                .screeningRoom(screeningRoom)
                .startTime(screeningDTO.getStartTime())
                .endTime(screeningDTO.getEndTime())
                .price(new BigDecimal(screeningDTO.getPrice()))
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
                .path(ScreeningController.MAIN_RESOURCE)
                .path(IDS_PATH)
                .buildAndExpand(id)
                .toUri();
    }

}
