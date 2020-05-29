import { Action } from 'redux'
import { movieListConstants } from '../../constants'
import {DateRange, MovieListFilters, PlayerMovies} from '../../../models/movies-list'

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
    playedMovies: PlayerMovies
}

export interface MovieListFiltersUpdateInterface extends Action {
    type: typeof movieListConstants.MOVIE_LIST_FILTERS_UPDATE,
    filters: MovieListFilters
}

export interface MovieListFiltersClearInterface extends Action {
    type: typeof movieListConstants.MOVIE_LIST_FILTERS_CLEAR
}

export type MovieListAction = MovieListRequestActionInterface | MovieListFailureActionInterface | MovieListSuccessActionInterface |
                              MovieListFiltersUpdateInterface | MovieListFiltersClearInterface
