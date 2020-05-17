import { movieListConstants } from '../../constants'
import { MovieList, DateRange } from '../../../models/movies-list'
import { Action, Dispatch } from 'redux'
import { MovieListService } from '../../../services'
import {
  MovieListFailureActionInterface,
  MovieListRequestActionInterface,
  MovieListSuccessActionInterface
} from './types'
export interface MovieListActionPublisher {
    getMovieList(dateRange: DateRange): (dispatch: Dispatch<Action>) => void;
}

export class MovieListActionPublisherImpl implements MovieListActionPublisher {
    movieListService: MovieListService;

    constructor (movieListService: MovieListService) {
      this.movieListService = movieListService
    }

    getMovieList (dateRange: DateRange): (dispatch: Dispatch<Action>) => void {
      return (dispatch: Dispatch<Action>) => {
        dispatch(request(dateRange))

        this.movieListService.getMovieList(dateRange)
          .then(
            (moviesList: MovieList) => {
              dispatch(success(moviesList))
            },
            (errorResponse: any) => {
              dispatch(failure(errorResponse.message))
            }
          )
      }

      function request (dateRange: DateRange): MovieListRequestActionInterface {
        return {
          type: movieListConstants.MOVIE_LIST_REQUEST,
          dateRange: dateRange
        }
      }

      function success (movieList: MovieList): MovieListSuccessActionInterface {
        return {
          type: movieListConstants.MOVIE_LIST_SUCCESS,
          movieList: movieList
        }
      }

      function failure (error: string): MovieListFailureActionInterface {
        return {
          type: movieListConstants.MOVIE_LIST_FAILURE,
          error: error
        }
      }
    }
}
