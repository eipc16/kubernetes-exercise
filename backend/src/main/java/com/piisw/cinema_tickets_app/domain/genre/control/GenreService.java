package com.piisw.cinema_tickets_app.domain.genre.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.infrastructure.bulk.BulkOperationResult;
import com.piisw.cinema_tickets_app.infrastructure.bulk.OperationResultEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenreService {

    private GenreRepository genreRepository;

    public List<Genre> getGenresByName(String genreName, Set<ObjectState> objectStates) {
        return genreRepository.findAllByNameLikeAndObjectStateIn("%" + genreName + "%", objectStates);
    }

    public Optional<Genre> createGenre(String genreName) {
        return createGenres(Collections.singleton(genreName)).getByOperationResult(OperationResultEnum.CREATED).stream()
                .findFirst();
    }

    public BulkOperationResult<Genre> createGenres(Collection<String> genreNames) {
        List<Genre> existingGenres = genreRepository.findAllByNameInAndObjectState(genreNames, ObjectState.ACTIVE);
        List<String> existingGenreNames = existingGenres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
        List<Genre> createdGenres = genreNames.stream()
                .filter(genre -> !existingGenreNames.contains(genre))
                .map(this::buildGenre)
                .collect(Collectors.toList());
        return BulkOperationResult.<Genre>builder()
                .addAllResults(OperationResultEnum.CREATED, genreRepository.saveAll(createdGenres))
                .addAllResults(OperationResultEnum.NOT_CREATED, existingGenres)
                .build();
    }

    private Genre buildGenre(String genreName) {
        return Genre.builder()
                .name(genreName)
                .objectState(ObjectState.ACTIVE)
                .build();
    }
}
