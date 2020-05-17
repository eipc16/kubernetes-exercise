import { Action } from 'redux'
import { movieListConstants } from '../../constants'
import { DateRange, MovieList } from '../../../models/movies-list'

export interface MovieListRequestActionInterface extends Action {
    type: typeof movieListConstants.MOVIE_LIST_REQUEST,
    dateRange: DateRange;
}

export interface MovieListFailureActionInterface extends Action {
    type: typeof movieListConstants.MOVIE_LIST_FAILURE,
    error: string;
}

export interface MovieListSuccessActionInterface extends Action {
    type: typeof movieListConstants.MOVIE_LIST_SUCCESS,
    movieList: MovieList
}

export type MovieListAction = MovieListRequestActionInterface | MovieListFailureActionInterface | MovieListSuccessActionInterface
