package com.piisw.cinema_tickets_app.domain.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieScreeningSearchParams {

    @NotNull
    private String searchText;

    @NotNull
    List<String> genres;

    @NotNull
    LocalDateTime beginDateTime;

    @NotNull
    LocalDateTime endDateTime;

    @NotNull
    Pageable pageable;
}
