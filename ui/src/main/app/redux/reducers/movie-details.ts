import {movieDetailsConstants} from '../constants'
import {MovieDetails} from "../../models/movie-details";
import {MovieDetailsAction, MovieDetailsSuccessActionInterface} from "../actions/movie-details";

const initialState = {
    isFetched: false,
    isFetching: false,
    movie: null
}

export interface MovieDetailsState {
    isFetching?: boolean;
    isFetched?: boolean;
    movie?: MovieDetails | null;
}

export function movieDetailsReducer(state: MovieDetailsState = initialState, action: MovieDetailsAction): MovieDetailsState {
    switch (action.type) {
        case movieDetailsConstants.MOVIE_DETAILS_SUCCESS:
            return {
                ...state,
                isFetched: true,
                isFetching: false,
                movie: (action as MovieDetailsSuccessActionInterface).movie
            }
        case movieDetailsConstants.MOVIE_DETAILS_REQUEST:
            return {
                ...state,
                isFetching: true
            }
        case movieDetailsConstants.MOVIE_DETAILS_FAILURE:
            return {
                ...state,
                isFetching: false
            }
        default:
            return state
    }
}
