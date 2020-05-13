import { MovieList, DateRange } from '../models/movies-list'
import { handleResponse } from './response-handler';
import { appConfig } from '../config';

export interface MovieListService {
    // get played movies
    getMovieList(dateRange: DateRange): Promise<MovieList>;
}

export class MovieListServiceImpl implements MovieListService {
    static movieListService: MovieListService;

    private constructor() {
    }

    static createInstance() {
        if (!MovieListServiceImpl.movieListService) {
            MovieListServiceImpl.movieListService = new MovieListServiceImpl();
        }
        return MovieListServiceImpl.movieListService;
    }


    getMovieList(dateRange: DateRange): Promise<MovieList> {
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dateRange)
        };
        return fetch(`${appConfig.apiUrl}/played`, requestOptions)
            .then(handleResponse)
            .then((moviesList: MovieList) => moviesList);
    }
}