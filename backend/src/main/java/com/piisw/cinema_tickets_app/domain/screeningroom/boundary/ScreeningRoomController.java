package com.piisw.cinema_tickets_app.domain.screeningroom.boundary;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.*;
import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.ScreeningRoomDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.HasAdminRole;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomService;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.infrastructure.security.validation.PermitAll;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Api(tags = "Screening Rooms")
@RestController
@RequestMapping(ScreeningRoomController.MAIN_RESOURCE)
@RequiredArgsConstructor
public class ScreeningRoomController {

    public static final String MAIN_RESOURCE = "/screening-rooms";

    private final ScreeningRoomService screeningRoomService;
    private final ScreeningRoomMapper screeningRoomMapper;

    @ApiOperation(value = "${api.screening.room.get.value}", notes = "${api.screening.room.get.notes}")
    @GetMapping(IDS_PATH)
    @PermitAll
    public List<ScreeningRoomDTO> getScreeningRoomsByIds(@ApiParam(value = "${api.screening.room.ids}") @PathVariable(IDS) Set<Long> ids,
            @ApiParam(value = "${api.screening.room.states}") @RequestParam(name = OBJECT_STATE, defaultValue = "ACTIVE") Set<ObjectState> objectStates) {
        return screeningRoomService.getScreeningRoomsByIds(ids, objectStates).stream()
                .map(screeningRoomMapper::mapToScreeningRoomDTO)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "${api.screening.room.post.value}", notes = "${api.screening.room.post.notes}")
    @PostMapping
    @HasAdminRole
    public ResourceDTO createScreeningRoom(@RequestBody @Validated ScreeningRoomDTO screeningRoomDTO) {
        ScreeningRoom screeningRoom = screeningRoomMapper.mapToScreeningRoom(screeningRoomDTO);
        ScreeningRoom createdScreeningRoom = screeningRoomService.createScreeningRoom(screeningRoom);
        return screeningRoomMapper.mapScreeningRoomToResourceDTO(createdScreeningRoom);
    }

    @ApiOperation(value = "${api.screening.room.put.value}", notes = "${api.screening.room.put.notes}")
    @PutMapping(ID_PATH)
    @HasAdminRole
    public ResourceDTO updateScreeningRoom(@PathVariable(ID) Long id, @Validated @RequestBody ScreeningRoomDTO screeningRoomDTO) {
        screeningRoomDTO.handleIdConsistency(id);
        ScreeningRoom screeningRoom = screeningRoomMapper.mapToScreeningRoom(screeningRoomDTO);
        ScreeningRoom updatedScreeningRoom = screeningRoomService.updateScreeningRoom(screeningRoom);
        return screeningRoomMapper.mapScreeningRoomToResourceDTO(updatedScreeningRoom);
    }

    @ApiOperation(value = "${api.screening.room.delete.value}", notes = "${api.screening.room.delete.notes}")
    @DeleteMapping(IDS_PATH)
    @HasAdminRole
    public List<ResourceDTO> deleteScreeningRoomByIds(@ApiParam(value = "${api.screening.room.rm.ids}") @PathVariable(IDS) Set<Long> ids) {
        return screeningRoomService.deleteScreeningRoomsByIds(ids).stream()
                .map(screeningRoomMapper::mapScreeningRoomToResourceDTO)
                .collect(Collectors.toList());
    }

}
