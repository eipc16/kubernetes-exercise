package com.piisw.cinema_tickets_app.domain.screeningroom.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "ScreeningRoom")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ScreeningRoom extends AuditedObject {

    @NotNull
    @Positive
    private Long number;

    @NotNull
    @Positive
    private Long rowsNumber;

    @NotNull
    @Positive
    private Long seatsInRowNumber;

}
