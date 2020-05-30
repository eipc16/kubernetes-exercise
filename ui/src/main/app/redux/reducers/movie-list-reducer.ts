import {movieListConstants} from '../constants'
import {MovieListAction, MovieListFiltersUpdateInterface, MovieListSuccessActionInterface} from '../actions/movie-list'
import {MovieListFilters, PlayerMovies} from '../../models/movies-list'

const WEEK_IN_MS = 604800000

const initialState = {
    isFetched: false,
    isFetching: false,
    playedMovies: null,
    filters: {
        dateRange: {
            beginDate: Date.now(),
            endDate: Date.now() + WEEK_IN_MS
        },
        pageOptions: {
            pageSize: 5,
            pageNumber: 0
        }
    }
}

export interface MovieListState {
    isFetching?: boolean;
    isFetched?: boolean;
    filters: MovieListFilters;
    playedMovies?: PlayerMovies | null;
}

export function movieListReducer(state: MovieListState = initialState, action: MovieListAction): MovieListState {
    switch (action.type) {
        case movieListConstants.MOVIE_LIST_SUCCESS:
            return {
                ...state,
                isFetched: true,
                isFetching: false,
                playedMovies: (action as MovieListSuccessActionInterface).playedMovies
            }
        case movieListConstants.MOVIE_LIST_REQUEST:
            return {
                ...state,
                isFetching: true
            }
        case movieListConstants.MOVIE_LIST_FAILURE:
            return {
                ...state,
                isFetching: false
            }
        case movieListConstants.MOVIE_LIST_FILTERS_UPDATE:
            console.log(action)
            return {
                ...state,
                filters: (action as MovieListFiltersUpdateInterface).filters
            }
        case movieListConstants.MOVIE_LIST_FILTERS_CLEAR:
            return {
                ...state,
                filters: initialState.filters
            }
        default:
            return state
    }
}
