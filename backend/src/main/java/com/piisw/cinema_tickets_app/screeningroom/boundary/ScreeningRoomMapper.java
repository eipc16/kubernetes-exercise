package com.piisw.cinema_tickets_app.screeningroom.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningRoomDTO;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class ScreeningRoomMapper {

    ScreeningRoomDTO mapToScreeningRoomDTO(ScreeningRoom screeningRoom) {
        return ScreeningRoomDTO.builder()
                .id(screeningRoom.getId())
                .number(screeningRoom.getNumber())
                .numberOfSeats(screeningRoom.getNumberOfSeats())
                .objectState(screeningRoom.getObjectState())
                .build();
    }

    ScreeningRoom mapToScreeningRoom(ScreeningRoomDTO screeningRoomDTO) {
        ScreeningRoom screeningRoom = ScreeningRoom.builder()
                .id(screeningRoomDTO.getId())
                .number(screeningRoomDTO.getNumber())
                .numberOfSeats(screeningRoomDTO.getNumberOfSeats())
                .objectState(screeningRoomDTO.getObjectState())
                .build();
        screeningRoomDTO.setObjectState(screeningRoomDTO.getObjectState());
        return screeningRoom;
    }

    ResourceDTO mapScreeningRoomToResourceDTO(ScreeningRoom screeningRoom) {
        return ResourceDTO.builder()
                .id(screeningRoom.getId())
                .identifier(screeningRoom.getNumber().toString())
                .uri(buildScreeningRoomUri(screeningRoom.getId()))
                .build();
    }

    private URI buildScreeningRoomUri(Long ...ids) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(ScreeningRoomController.MAIN_PATH)
                .path(ScreeningRoomController.IDS_PATH)
                .buildAndExpand(ids)
                .toUri();
    }

}
