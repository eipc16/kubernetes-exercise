import { handleResponse } from './response-handler'
import { appConfig } from '../config'
import {MovieDetailsList} from "../models/movie-details";
import {AuthenticationService, AuthenticationServiceImpl} from "./auth-service";

export interface MovieDetailsService {
    getMovieDetails(id: number): Promise<MovieDetailsList>;
}

export class MovieDetailsServiceImpl implements MovieDetailsService {
    static movieDetailsService: MovieDetailsService;
    private authService: AuthenticationService;

    private constructor (authService?: AuthenticationService) {
        this.authService = authService || AuthenticationServiceImpl.createInstance();
    }

    static createInstance (authService?: AuthenticationService): MovieDetailsService {
        if (!MovieDetailsServiceImpl.movieDetailsService) {
            MovieDetailsServiceImpl.movieDetailsService = new MovieDetailsServiceImpl(authService)
        }
        return MovieDetailsServiceImpl.movieDetailsService
    }

    getMovieDetails(id: number): Promise<MovieDetailsList> {
        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'Authorization': this.authService.getCurrentTokenAsString() }
        }
        return fetch(`${appConfig.apiUrl}/movies/${id}/details`, requestOptions)
            .then(handleResponse)
            .then((movie: MovieDetailsList) => movie)
    }
}
