package com.piisw.cinema_tickets_app.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;

    @NotNull
    private Long seatNumber;

    @NotNull
    private Long rowNumber;

    @NotNull
    boolean isAvailable;

}
