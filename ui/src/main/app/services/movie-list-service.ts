import { MovieList, DateRange } from '../models/movies-list'
import { handleResponse } from './response-handler'
import { appConfig } from '../config'

export interface MovieListService {
    // get played movies
    getMovieList(dateRange: DateRange): Promise<MovieList>;
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

    getMovieList (dateRange: DateRange): Promise<MovieList> {
      const requestOptions = {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      }
      const beginDate = new Date(dateRange.beginDate).toISOString()
      const endDate = new Date(dateRange.endDate).toISOString()

      return fetch(`${appConfig.apiUrl}/movies/played?beginDate=${beginDate}&endDate=${endDate}`, requestOptions)
        .then(handleResponse)
        .then((movieList: MovieList) => movieList)
    }
}
