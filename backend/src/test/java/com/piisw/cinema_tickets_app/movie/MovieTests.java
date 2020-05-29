package com.piisw.cinema_tickets_app.movie;

import com.piisw.cinema_tickets_app.api.MovieDetailsDTO;
import com.piisw.cinema_tickets_app.client.OpenMovieDatabaseClient;
import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.genre.control.GenreService;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieService;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieToGenreRelationService;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.bulk.BulkOperationResult;
import com.piisw.cinema_tickets_app.infrastructure.bulk.OperationResultEnum;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({MovieService.class, AuditedObjectSpecification.class, AuditingConfig.class, UserService.class,
        BCryptPasswordEncoder.class, GenreService.class, MovieToGenreRelationService.class})
public class MovieTests {

    @Autowired
    private MovieService movieService;

    @MockBean
    private OpenMovieDatabaseClient mockedClient;

    @Test
    public void shouldCreateMovie() {
        MovieDetailsDTO movieDetails = getDummyMovieDetails();
        whenPerformingClientCallReturn(movieDetails);
        BulkOperationResult<Movie> operationResult = movieService.createMovies(Set.of("dummy"));
        Movie createdMovie = operationResult.getByOperationResult(OperationResultEnum.CREATED)
                .stream()
                .findAny()
                .orElseThrow();
        assertNotNull(createdMovie.getId());
        assertEquals(movieDetails.getImdbId(), createdMovie.getImdbId());
        assertEquals(movieDetails.getTitle(), createdMovie.getTitle());
        assertEquals(movieDetails.getYear(), createdMovie.getYear());
        assertEquals(movieDetails.getMaturityRate(), createdMovie.getMaturityRating());
        assertEquals(movieDetails.getReleaseDate(), createdMovie.getReleaseDate());
        assertEquals(movieDetails.getReleaseDate(), createdMovie.getReleaseDate());
        assertEquals(movieDetails.getRuntime(), createdMovie.getRunTime());
        assertEquals(movieDetails.getDirector(), createdMovie.getDirector());
        assertEquals(movieDetails.getPlot(), createdMovie.getShortPlot());
        assertEquals(movieDetails.getLanguage(), createdMovie.getLanguage());
        assertEquals(movieDetails.getPosterLink(), createdMovie.getPosterUrl());
        assertEquals(movieDetails.getActors(), createdMovie.getActors());
        assertEquals(movieDetails.getCountry(), createdMovie.getCountry());
    }

    private void whenPerformingClientCallReturn(MovieDetailsDTO movieDetails) {
        when(mockedClient.getMovieDetailsByImdbIds(any())).thenReturn(List.of(movieDetails));
    }

    private MovieDetailsDTO getDummyMovieDetails() {
        return MovieDetailsDTO.builder()
                .imdbId("tt1375666")
                .title("Inception")
                .year("2010")
                .maturityRate("PG-13")
                .releaseDate(LocalDate.now())
                .runtime("148 min")
                .director("Christopher Nolan")
                .plot("A thief who steals corporate secrets through the use of dream-sharing technology"
                        + "is given the inverse task of planting an idea into the mind of a C.E.O.")
                .language("English, Japanese, French")
                .posterLink("https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg")
                .actors("Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page, Tom Hardy")
                .country("USA, UK")
                .genres(List.of("Action", "Adventure", "Sci-Fi", "Thriller"))
                .build();
    }

    @Test
    public void shouldDeleteMovie() {
        MovieDetailsDTO movieDetails = getDummyMovieDetails();
        whenPerformingClientCallReturn(movieDetails);
        Set<Movie> createdMovies = movieService.createMovies(Set.of("dummy"))
                .getByOperationResult(OperationResultEnum.CREATED);
        Set<Long> createdMoviesIds = createdMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.toSet());
        List<Movie> removedMovies = movieService.deleteMoviesByIds(createdMoviesIds);
        boolean allMoviesRemovedObjectState = removedMovies.stream()
                .map(Movie::getObjectState)
                .allMatch(ObjectState.REMOVED::equals);
        boolean allWereRemoved = removedMovies.stream()
                .map(Movie::getId)
                .collect(Collectors.toSet())
                .equals(createdMoviesIds);
        assertTrue(allMoviesRemovedObjectState && allWereRemoved);
    }


}