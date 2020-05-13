import { movieListConstants } from '../constants';
import { MovieListSuccessActionInterface } from '../actions/movie-list';
import { MovieList } from "../../models/movies-list";

const initialState
    = { isFetched: false, isFetching: false, moviesList: null };

export interface MovieListState {
    isFetching?: boolean;
    isFetched?: boolean;
    movieList?: MovieList;
}

export function movieListReducer(state: MovieListState = initialState, action: MovieListSuccessActionInterface ): MovieListState {
    switch(action.type) {
        case movieListConstants.MOVIE_LIST_SUCCESS:
            return {
                isFetched: true,
                isFetching: false,
                movieList: action.movieList
            };
        case movieListConstants.MOVIE_LIST_REQUEST:
            return {
                isFetching: true,
            };
        case movieListConstants.MOVIE_LIST_FAILURE:
            return {
                isFetching: false
            };
        default:
            return state;
    }
}