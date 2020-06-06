import { PlayerMovies, DateRange } from '../models/movies-list'
import { handleResponse } from './response-handler'
import { appConfig } from '../config'
import {Pageable} from "../models/infrastructure";

export interface MovieListService {
    // get played movies
    getMovieList(dateRange: DateRange, searchText?: string, genres?: string[], pageOptions?: Pageable): Promise<PlayerMovies>;
}

export class MovieListServiceImpl implements MovieListService {
    static movieListService: MovieListService;

    private constructor () {
    }

    static createInstance () {
      if (!MovieListServiceImpl.movieListService) {
        MovieListServiceImpl.movieListService = new MovieListServiceImpl()
      }
      return MovieListServiceImpl.movieListService
    }

    getMovieList (dateRange: DateRange, searchText?: string, genres?: string[], pageOptions?: Pageable): Promise<PlayerMovies> {
      const requestOptions = {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      }
      const beginDate = new Date(dateRange.beginDate).toISOString()
      const endDate = new Date(dateRange.endDate).toISOString()
        let queryParams = `beginDate=${beginDate}&endDate=${endDate}`;
        if(searchText) {
            queryParams += `&searchText=${searchText}`
        }
        if(genres && genres.length > 0) {
            queryParams += `&genres=${genres.join(",")}`
        }
        if(pageOptions) {
            queryParams += `&page=${pageOptions.pageNumber}&size=${pageOptions.pageSize}`
        }
        return fetch(`${appConfig.apiUrl}/movies/played?${queryParams}`, requestOptions)
        .then(handleResponse)
        .then((movieList: PlayerMovies) => movieList)
    }
}
