import { handleResponse } from './response-handler'
import { appConfig } from '../config'
import { MovieDetails } from "../models/movie-details";

export interface MovieDetailsService {
    getMovieDetails(id: String): Promise<MovieDetails>;
}

export class MovieDetailsServiceImpl implements MovieDetailsService {
    static movieDetailsService: MovieDetailsService;

    private constructor () {
    }

    static createInstance () {
        if (!MovieDetailsServiceImpl.movieDetailsService) {
            MovieDetailsServiceImpl.movieDetailsService = new MovieDetailsServiceImpl()
        }
        return MovieDetailsServiceImpl.movieDetailsService
    }

    getMovieDetails(id: String): Promise<MovieDetails> {
        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        }
        return fetch(`${appConfig.apiUrl}/movies/${id}/details`, requestOptions)
            .then(handleResponse)
            .then((movie: MovieDetails) => movie)
    }
}
