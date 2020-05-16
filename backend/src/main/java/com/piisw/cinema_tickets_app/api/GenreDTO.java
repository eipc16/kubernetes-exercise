package com.piisw.cinema_tickets_app.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDTO {

    @NotNull
    private Long id;

    @NotNull
    private String name;
}
