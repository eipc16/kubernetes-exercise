package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MovieDTO {

    @ApiModelProperty(readOnly = true)
    private Long id;
    private String imdbId;
    private ObjectState objectState;
    private MovieDetailsDTO details;

}
