package com.piisw.cinema_tickets_app.genre;

import com.piisw.cinema_tickets_app.domain.authentication.control.AuthenticationService;
import com.piisw.cinema_tickets_app.domain.genre.control.GenreService;
import com.piisw.cinema_tickets_app.domain.genre.entity.Genre;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.bulk.BulkOperationResult;
import com.piisw.cinema_tickets_app.infrastructure.bulk.OperationResultEnum;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
@Import({AuthenticationService.class, UserService.class, BCryptPasswordEncoder.class, AuditingConfig.class, GenreService.class})
public class GenreTests {

    @Autowired
    private GenreService genreService;

    @Test
    public void shouldCreateGenres() {
        Set<String> dummyGenres = Set.of("Action", "Comedy", "Thriller");
        BulkOperationResult<Genre> operationResult = genreService.createGenres(dummyGenres);
        boolean createdAllSuppliedGenres = operationResult.getByOperationResult(OperationResultEnum.CREATED)
                .stream()
                .map(Genre::getName)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), dummyGenres::equals));
        assertTrue("Not all expected genres were created", createdAllSuppliedGenres);
    }

    @Test
    public void shouldNotCreateAlreadyExistingGenres() {
        Set<String> dummyGenres = Set.of("Action", "Comedy", "Thriller");
        genreService.createGenres(dummyGenres);
        Set<String> partiallyRepeatedDummyGenres = Set.of("Action", "Horror");
        BulkOperationResult<Genre> operationResult = genreService.createGenres(partiallyRepeatedDummyGenres);
        boolean createdNonExistingGenres = operationResult.getByOperationResult(OperationResultEnum.CREATED)
                .stream()
                .map(Genre::getName)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Set.of("Horror")::equals));
        boolean notCreatedAlreadyExistingGenres = operationResult.getByOperationResult(OperationResultEnum.NOT_CREATED)
                .stream()
                .map(Genre::getName)
                .collect(Collectors.collectingAndThen(Collectors.toSet(), Set.of("Action")::equals));
        assertTrue("Not created not existing genres", createdNonExistingGenres);
        assertTrue("Created already existing genres", notCreatedAlreadyExistingGenres);
    }

}
