package com.piisw.cinema_tickets_app.screeningroom.boundary;

import com.piisw.cinema_tickets_app.api.ScreeningRoomDTO;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import org.springframework.stereotype.Component;

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
                .number(screeningRoomDTO.getNumber())
                .numberOfSeats(screeningRoomDTO.getNumberOfSeats())
                .build();
        screeningRoomDTO.setObjectState(screeningRoomDTO.getObjectState());
        return screeningRoom;
    }

}
