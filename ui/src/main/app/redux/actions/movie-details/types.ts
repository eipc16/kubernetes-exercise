import { Action } from 'redux'
import { movieDetailsConstants } from '../../constants'
import {MovieDetails} from "../../../models/movie-details";

export interface MovieDetailsRequestActionInterface extends Action {
    type: typeof movieDetailsConstants.MOVIE_DETAILS_REQUEST,
    id: number;
}

export interface MovieDetailsFailureActionInterface extends Action {
    type: typeof movieDetailsConstants.MOVIE_DETAILS_FAILURE,
    error: string;
}

export interface MovieDetailsSuccessActionInterface extends Action {
    type: typeof movieDetailsConstants.MOVIE_DETAILS_SUCCESS,
    movie: MovieDetails
}

export type MovieDetailsAction = MovieDetailsFailureActionInterface | MovieDetailsRequestActionInterface
    | MovieDetailsSuccessActionInterface
