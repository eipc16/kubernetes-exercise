package com.piisw.cinema_tickets_app.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;
    private Long screeningId;
    private Set<Long> seatsIds;
    private Long reservedByUser;

}
