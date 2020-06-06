package com.piisw.cinema_tickets_app.domain.movie.boundary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieScreeningSearchParams {

    private String searchText;
    List<String> genres;
    LocalDateTime beginDateTime;
    LocalDateTime endDateTime;
    Pageable pageable;
}
