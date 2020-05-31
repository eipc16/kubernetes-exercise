package com.piisw.cinema_tickets_app.domain.user.boundary;

import com.piisw.cinema_tickets_app.api.ResourceDTO;
import com.piisw.cinema_tickets_app.api.UserDTO;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.user.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.ID_PATH;
import static com.piisw.cinema_tickets_app.infrastructure.utils.ResourcePath.OBJECT_STATE;

@Component
public class UserMapper {

    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getUserRole().name())
                .objectState(user.getObjectState())
                .build();
    }

    public ResourceDTO mapToResourceDTO(User user) {
        return ResourceDTO.builder()
                .id(user.getId())
                .identifier(user.getUsername())
                .uri(buildUri(user.getId()))
                .build();
    }

    private URI buildUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(UserController.MAIN_RESOURCE)
                .path(ID_PATH)
                .queryParam(OBJECT_STATE, ObjectState.values())
                .buildAndExpand(id)
                .toUri();
    }

}
