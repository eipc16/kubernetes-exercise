import {Action, Dispatch} from 'redux'
import {
    MovieDetailsFailureActionInterface,
    MovieDetailsRequestActionInterface,
    MovieDetailsSuccessActionInterface
} from './types'
import {MovieDetailsService} from "../../../services/movie-details-service";
import {MovieDetailsList} from "../../../models/movie-details";
import {movieDetailsConstants} from "../../constants";
import {MovieDetailsListInterface} from "../../../models/movie-details/movie-details-list";

export interface MovieDetailsActionPublisher {
    getMovieDetails(id: number): (dispatch: Dispatch<Action>) => Promise<MovieDetailsListInterface | void>;
}

export class MovieDetailsActionPublisherImpl implements MovieDetailsActionPublisher {
    movieDetailsService: MovieDetailsService;

    constructor(movieDetailsService: MovieDetailsService) {
        this.movieDetailsService = movieDetailsService;
    }

    getMovieDetails(id: number): (dispatch: Dispatch<Action>) => Promise<MovieDetailsListInterface | void> {
        function request(id: number): MovieDetailsRequestActionInterface {
            return {
                type: movieDetailsConstants.MOVIE_DETAILS_REQUEST,
                id: id
            }
        }

        function success(movie: MovieDetailsList): MovieDetailsSuccessActionInterface {
            return {
                type: movieDetailsConstants.MOVIE_DETAILS_SUCCESS,
                movie: movie
            }
        }

        function failure(error: string): MovieDetailsFailureActionInterface {
            return {
                type: movieDetailsConstants.MOVIE_DETAILS_FAILURE,
                error: error
            }
        }

        return (dispatch: Dispatch<Action>): Promise<MovieDetailsListInterface | void> => {
            dispatch(request(id));
            return this.movieDetailsService.getMovieDetails(id)
                .then((movie: MovieDetailsList) => {
                        dispatch(success(movie));
                        return movie;
                    }
                ).catch((errorResponse: string) => {
                    dispatch(failure(errorResponse));
                })
        };
    }
}
