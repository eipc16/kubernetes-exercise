package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;

    @NotNull
    private Long screeningId;

    @NotNull
    private Set<Long> seatsIds;

    private Long reservedByUser;

    @ApiModelProperty(readOnly = true)
    private ObjectState objectState;

}
