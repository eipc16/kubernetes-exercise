package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.auditedobject.AuditedObjectState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
public class ScreeningRoomDTO {

    private Long id;

    @NotNull
    @Positive
    private Long number;

    @NotNull
    @Positive
    private Long numberOfSeats;

    AuditedObjectState objectState;

}
