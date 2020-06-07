import {appConfig} from "../config";
import {handleResponse} from "./response-handler";
import {Genre} from "../models/genre";
import {AuthenticationService, AuthenticationServiceImpl} from "./auth-service";

export interface GenreService {
    // all genres
    fetchGenres(searchText?: string): Promise<Genre[]>;
}

export class GenreServiceImpl implements GenreService {
    static genreService: GenreService;
    private authService: AuthenticationService;

    private constructor(authenticationService?: AuthenticationService) {
        this.authService = authenticationService || AuthenticationServiceImpl.createInstance();
    }

    static createInstance(authenticationService?: AuthenticationService): GenreService {
        if (!GenreServiceImpl.genreService) {
            GenreServiceImpl.genreService = new GenreServiceImpl(authenticationService)
        }
        return GenreServiceImpl.genreService
    }

    fetchGenres(searchText?: string): Promise<Genre[]> {
        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'Authorization': this.authService.getCurrentTokenAsString() }
        }
        let queryParams = '';
        if(searchText) {
            queryParams += `searchText=${searchText}`
        }
        return fetch(`${appConfig.apiUrl}/genres?${queryParams}`, requestOptions)
            .then(handleResponse)
            .then((genres: Genre[]) => genres)
    }
}