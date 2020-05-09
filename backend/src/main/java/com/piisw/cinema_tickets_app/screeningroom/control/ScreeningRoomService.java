package com.piisw.cinema_tickets_app.screeningroom.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObjectState;
import com.piisw.cinema_tickets_app.screeningroom.entity.ScreeningRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ScreeningRoomService {

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    public List<ScreeningRoom> getAllScreeningRoomsByIdsAndObjectStates(Set<Long> ids, Set<AuditedObjectState> objectStates) {
        return screeningRoomRepository.findAll(ScreeningRoomSpecifications.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

    public ScreeningRoom createScreeningRoom(ScreeningRoom screeningRoom) {
        screeningRoom.setObjectState(AuditedObjectState.ACTIVE);
        return screeningRoomRepository.save(screeningRoom);
    }

}
