package com.piisw.cinema_tickets_app.domain.genre.boundary;

import com.piisw.cinema_tickets_app.api.GenreDTO;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenreMapper {
    public List<String> mapToGenreNames(Collection<Genre> genres) {
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }

    public List<GenreDTO> mapToGenreDTOs(Collection<Genre> genres) {
        return genres.stream()
                .map(this::mapToGenreDTO)
                .collect(Collectors.toList());
    }

    private GenreDTO mapToGenreDTO(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
