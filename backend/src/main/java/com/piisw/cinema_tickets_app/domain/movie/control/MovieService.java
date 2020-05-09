package com.piisw.cinema_tickets_app.domain.movie.control;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import com.piisw.cinema_tickets_app.domain.movie.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getMoviesByIds(Set<Long> ids, Set<ObjectState> objectStates) {
        return movieRepository.findAll(MovieSpecifications.hasIdInSetAndObjectStateInSet(ids, objectStates));
    }

}
