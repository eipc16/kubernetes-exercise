package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class ScreeningRoomDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;

    @NotNull
    @Positive
    private Long number;

    @NotNull
    private Long rowsNumber;

    @NotNull
    private Long seatsInRowNumber;

    @ApiModelProperty(readOnly = true)
    ObjectState objectState;

    public void handleIdConsistency(Long expectedId) {
        if (!Objects.equals(expectedId, id)) {
            if (id == null) {
                id = expectedId;
            } else {
                throw new IllegalArgumentException("Supplied ids are inconsistent!");
            }
        }
    }

}
