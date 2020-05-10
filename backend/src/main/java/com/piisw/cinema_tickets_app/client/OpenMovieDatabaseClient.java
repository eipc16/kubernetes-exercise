package com.piisw.cinema_tickets_app.client;

import com.piisw.cinema_tickets_app.api.MovieDetailsDTO;
import com.piisw.cinema_tickets_app.api.RatingDTO;
import com.piisw.cinema_tickets_app.client.api.OpenApiMovieDTO;
import com.piisw.cinema_tickets_app.client.api.OpenApiRatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OpenMovieDatabaseClient {

    @Value("${omdb.api.key}")
    private String apiKey;

    public static final String HTTP_SCHEME = "http";
    private static final String OMDB_API_ADDRESS = "www.omdbapi.com";
    private static final String ID_PARAM = "i";
    private static final String API_KEY_PARAM = "apiKey";

    @Autowired
    private RestTemplate restTemplate;

    public List<MovieDetailsDTO> getMovieDetailsByImdbIds(Set<String> imdbIds) {
        return imdbIds.stream()
                .map(this::getMovieDetailsByImdbId)
                .collect(Collectors.toList());
    }

    public MovieDetailsDTO getMovieDetailsByImdbId(String imdbId) {
       OpenApiMovieDTO openApiMovieDTO = restTemplate.getForEntity(getOpenApiUri(imdbId), OpenApiMovieDTO.class).getBody();
       return mapToMovieDetailsDTO(openApiMovieDTO);
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
                .maturityRate(openApiMovieDTO.getRated())
                .releaseDate(openApiMovieDTO.getReleased())
                .runtime(openApiMovieDTO.getRuntime())
                .genre(openApiMovieDTO.getGenre())
                .director(openApiMovieDTO.getDirector())
                .writer(openApiMovieDTO.getWriter())
                .actors(openApiMovieDTO.getActors())
                .plot(openApiMovieDTO.getPlot())
                .language(openApiMovieDTO.getLanguage())
                .country(openApiMovieDTO.getCountry())
                .awards(openApiMovieDTO.getAwards())
                .posterLink(openApiMovieDTO.getPoster())
                .ratings(mapToRatingDTOs(openApiMovieDTO.getRatings()))
                .metaScore(openApiMovieDTO.getMetascore())
                .ibmRating(openApiMovieDTO.getImdbRating())
                .producer(openApiMovieDTO.getProduction())
                .build();
    }

    private Set<RatingDTO> mapToRatingDTOs(Set<OpenApiRatingDTO> openApiRatings) {
        return openApiRatings.stream()
                .map(this::mapToRatingDTO)
                .collect(Collectors.toSet());
    }

    private RatingDTO mapToRatingDTO(OpenApiRatingDTO openApiRatingDTO) {
        return RatingDTO.builder()
                .source(openApiRatingDTO.getSource())
                .rate(openApiRatingDTO.getRate())
                .build();
    }
}
