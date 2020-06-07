import {movieListConstants} from '../../constants'
import {DateRange, MovieListFilters, PlayerMovies} from '../../../models/movies-list'
import {Action, Dispatch} from 'redux'
import {MovieListService} from '../../../services'
import {
    MovieListFailureActionInterface,
    MovieListFiltersClearInterface,
    MovieListFiltersUpdateInterface,
    MovieListRequestActionInterface,
    MovieListSuccessActionInterface
} from './types'
import {Pageable} from "../../../models/infrastructure";

export interface MovieListActionPublisher {
    getMovieListByFilters(filters: MovieListFilters): (dispatch: Dispatch<Action>) => void;

    getMovieList(dateRange: DateRange, searchText?: string, genres?: string[], pageOptions?: Pageable): (dispatch: Dispatch<Action>) => void;

    updateFilters(filters: MovieListFilters): MovieListFiltersUpdateInterface;

    clearFilters(): MovieListFiltersClearInterface;
}

export class MovieListActionPublisherImpl implements MovieListActionPublisher {
    movieListService: MovieListService;

    constructor(movieListService: MovieListService) {
        this.movieListService = movieListService;
    }

    getMovieListByFilters(filters: MovieListFilters): (dispatch: Dispatch<Action>) => void {
        return this.getMovieList({
            beginDate: filters.dateRange.beginDate,
            endDate: filters.dateRange.endDate
        }, filters.searchText, filters.genres, filters.pageOptions);
    }

    getMovieList(dateRange: DateRange, searchText?: string, genres?: string[], pageOptions?: Pageable): (dispatch: Dispatch<Action>) => void {
        function request(dateRange: DateRange): MovieListRequestActionInterface {
            return {
                type: movieListConstants.MOVIE_LIST_REQUEST,
                dateRange: dateRange
            }
        }

        function success(movieList: PlayerMovies): MovieListSuccessActionInterface {
            return {
                type: movieListConstants.MOVIE_LIST_SUCCESS,
                playedMovies: movieList
            }
        }

        function failure(error: string): MovieListFailureActionInterface {
            return {
                type: movieListConstants.MOVIE_LIST_FAILURE,
                error: error
            }
        }

        return (dispatch: Dispatch<Action>): void => {
            dispatch(request(dateRange));

            this.movieListService.getMovieList(dateRange, searchText, genres, pageOptions)
                .then(
                    (moviesList: PlayerMovies) => {
                        dispatch(success(moviesList));
                    },
                    (errorResponse: string) => {
                        dispatch(failure(errorResponse));
                    }
                )
        };
    }

    clearFilters(): MovieListFiltersClearInterface {
        return {
            type: movieListConstants.MOVIE_LIST_FILTERS_CLEAR
        };
    }

    updateFilters(filters: MovieListFilters): MovieListFiltersUpdateInterface {
        return {
            type: movieListConstants.MOVIE_LIST_FILTERS_UPDATE,
            filters: filters
        };
    }
}
