package com.piisw.cinema_tickets_app.client;

import com.piisw.cinema_tickets_app.api.MovieDetailsDTO;
import com.piisw.cinema_tickets_app.client.api.OpenApiMovieDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OpenMovieDatabaseClient {

    public static final String HTTP_SCHEME = "http";
    private static final String OMDB_API_ADDRESS = "www.omdbapi.com";
    private static final String ID_PARAM = "i";
    private static final String API_KEY_PARAM = "apiKey";

    @Value("${omdb.api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    public List<MovieDetailsDTO> getMovieDetailsByImdbIds(Collection<String> imdbIds) {
        return imdbIds.stream()
                .map(this::getMovieDetailsByImdbId)
                .flatMap(Optional::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<MovieDetailsDTO> getMovieDetailsByImdbId(String imdbId) {
        return getOpenApiMovieByImdbId(imdbId).map(this::mapToMovieDetailsDTO);
    }

    private Optional<OpenApiMovieDTO> getOpenApiMovieByImdbId(String imdbId) {
        URI requestURI = getOpenApiUri(imdbId);
        OpenApiMovieDTO openApiMovie = restTemplate.getForEntity(requestURI, OpenApiMovieDTO.class).getBody();
        return Optional.ofNullable(openApiMovie)
                .filter(openMovie -> "True".equals(openMovie.getResponse()) && "movie".equals(openMovie.getType()));
    }

    private URI getOpenApiUri(String id) {
        return UriComponentsBuilder.newInstance()
                .scheme(HTTP_SCHEME)
                .host(OMDB_API_ADDRESS)
                .queryParam(ID_PARAM, id)
                .queryParam(API_KEY_PARAM, apiKey)
                .build()
                .toUri();
    }

    private MovieDetailsDTO mapToMovieDetailsDTO(OpenApiMovieDTO openApiMovieDTO) {
        return MovieDetailsDTO.builder()
                .imdbId(openApiMovieDTO.getImdbId())
                .title(openApiMovieDTO.getTitle())
                .year(openApiMovieDTO.getYear())
                .maturityRate(openApiMovieDTO.getRated())
                .releaseDate(openApiMovieDTO.getReleased())
                .runtime(openApiMovieDTO.getRuntime())
                .genres(openApiMovieDTO.getAllGenreNames())
                .director(openApiMovieDTO.getDirector())
                .actors(openApiMovieDTO.getActors())
                .plot(openApiMovieDTO.getPlot())
                .language(openApiMovieDTO.getLanguage())
                .country(openApiMovieDTO.getCountry())
                .posterLink(openApiMovieDTO.getPoster())
                .build();
    }
}
