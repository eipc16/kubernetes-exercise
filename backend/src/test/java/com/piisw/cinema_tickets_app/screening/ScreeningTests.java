package com.piisw.cinema_tickets_app.screening;

import com.piisw.cinema_tickets_app.domain.auditedobject.control.AuditedObjectSpecification;
import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.control.MovieRepository;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningService;
import com.piisw.cinema_tickets_app.domain.screening.control.ScreeningSpecification;
import com.piisw.cinema_tickets_app.domain.screening.entity.Screening;
import com.piisw.cinema_tickets_app.domain.screeningroom.control.ScreeningRoomRepository;
import com.piisw.cinema_tickets_app.domain.screeningroom.entity.ScreeningRoom;
import com.piisw.cinema_tickets_app.domain.user.control.UserService;
import com.piisw.cinema_tickets_app.infrastructure.configuration.AuditingConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ScreeningService.class, AuditedObjectSpecification.class, AuditingConfig.class, UserService.class,
        BCryptPasswordEncoder.class, ScreeningSpecification.class})
public class ScreeningTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreeningRoomRepository screeningRoomRepository;

    @Autowired
    private ScreeningService screeningService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void shouldCreateScreening() {
        ScreeningRoom screeningRoom = screeningRoomRepository.save(getDummyScreeningRoom());
        Movie movie = movieRepository.save(getDummyMovie());
        Screening screening = getDummyScreening(screeningRoom, movie);
        Screening createdScreening = screeningService.createScreening(screening);
        assertEquals(screening.getScreeningRoom().getId(), createdScreening.getScreeningRoom().getId());
        assertEquals(screening.getMovie().getId(), createdScreening.getMovie().getId());
        assertEquals(screening.getPrice(), createdScreening.getPrice());
        assertEquals(ObjectState.ACTIVE, createdScreening.getObjectState());
        assertNotNull(createdScreening.getId());
    }

    public static Screening getDummyScreening(ScreeningRoom screeningRoom, Movie movie) {
        return Screening.builder()
                .screeningRoom(screeningRoom)
                .movie(movie)
                .startTime(LocalDateTime.now())
                .price(BigDecimal.TEN)
                .build();
    }

    public static ScreeningRoom getDummyScreeningRoom() {
        return ScreeningRoom.builder()
                .number(23L)
                .rowsNumber(10L)
                .seatsInRowNumber(10L)
                .build();
    }

    public static Movie getDummyMovie() {
        return Movie.builder()
                .imdbId("tt1375666")
                .title("Inception")
                .year("2010")
                .maturityRating("PG-13")
                .releaseDate(LocalDate.now())
                .runTime("148 min")
                .director("Christopher Nolan")
                .shortPlot("A thief who steals corporate secrets through the use of dream-sharing technology"
                        + "is given the inverse task of planting an idea into the mind of a C.E.O.")
                .language("English, Japanese, French")
                .posterUrl("https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg")
                .actors("Leonardo DiCaprio, Joseph Gordon-Levitt, Ellen Page, Tom Hardy")
                .country("USA, UK")
                .build();
    }

    @Test
    public void shouldReturnScreeningsForMovie() {
        ScreeningRoom screeningRoom = screeningRoomRepository.save(getDummyScreeningRoom());
        Movie movie = movieRepository.save(getDummyMovie());
        Screening screening = getDummyScreening(screeningRoom, movie);
        Screening createdScreening = screeningService.createScreening(screening);
        List<Screening> screeningsForMovie = screeningService.getScreeningsByMovie(movie, Set.of(ObjectState.ACTIVE));
        boolean foundScreening = screeningsForMovie.stream()
                .map(Screening::getId)
                .anyMatch(createdScreening.getId()::equals);
        assertTrue("Screening for movie not found", foundScreening);
    }

    @Test
    public void shouldReturnScreeningsForScreeningRoom() {
        ScreeningRoom screeningRoom = screeningRoomRepository.save(getDummyScreeningRoom());
        Movie movie = movieRepository.save(getDummyMovie());
        Screening screening = getDummyScreening(screeningRoom, movie);
        Screening createdScreening = screeningService.createScreening(screening);
        List<Screening> screeningsForScreeningRoom = screeningService.getScreeningByScreeningRoomId(screeningRoom.getId(), ObjectState.ACTIVE);
        boolean foundScreening = screeningsForScreeningRoom.stream()
                .map(Screening::getId)
                .anyMatch(createdScreening.getId()::equals);
        assertTrue("Screening for screening room not found", foundScreening);
    }

    @Test
    public void shouldReturnScreeningsForTimePeriod() {
        ScreeningRoom screeningRoom = screeningRoomRepository.save(getDummyScreeningRoom());
        Movie movie = movieRepository.save(getDummyMovie());
        Screening screening1 = getDummyScreening(screeningRoom, movie);
        Screening screening2 = getDummyScreening(screeningRoom, movie)
                .toBuilder()
                .startTime(LocalDateTime.now().plusDays(2))
                .build();
        Screening screening3 = getDummyScreening(screeningRoom, movie)
                .toBuilder()
                .startTime(LocalDateTime.now().plusDays(3))
                .build();
        screening1 = screeningService.createScreening(screening1);
        screening2 = screeningService.createScreening(screening2);
        screening3 = screeningService.createScreening(screening3);
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);
        List<Screening> screeningsForTimePeriod = screeningService.getScreeningsWhereStartTimeIsBetween(startTime, endTime);
        Set<Long> foundScreeningIds = screeningsForTimePeriod.stream()
                .map(Screening::getId)
                .collect(Collectors.toSet());
        boolean foundCorrectScreenings = foundScreeningIds.contains(screening1.getId()) &&
                foundScreeningIds.contains(screening2.getId()) &&
                !foundScreeningIds.contains(screening3.getId());
        assertTrue("Not found corrected screenings for time period", foundCorrectScreenings);
    }

}
