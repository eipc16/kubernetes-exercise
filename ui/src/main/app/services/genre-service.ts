import {appConfig} from "../config";
import {handleResponse} from "./response-handler";
import {Genre} from "../models/genre";

export interface GenreService {
    // all genres
    fetchGenres(searchText?: string): Promise<Genre[]>;
}

export class GenreServiceImpl implements GenreService {
    static genreService: GenreService;

    private constructor () {
    }

    static createInstance () {
        if (!GenreServiceImpl.genreService) {
            GenreServiceImpl.genreService = new GenreServiceImpl()
        }
        return GenreServiceImpl.genreService
    }

    fetchGenres(searchText?: string): Promise<Genre[]> {
        const requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
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