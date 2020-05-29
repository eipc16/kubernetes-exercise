import { movieListReducer } from '../movie-list-reducer'
import { movieListConstants } from "../../constants";

describe('Movie list reducer', () => {
    it('should handle MOVIE_LIST_REQUEST', () => {
        expect(
            movieListReducer({
                isFetching: false,
                isFetched: false,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: null}, {
                type: movieListConstants.MOVIE_LIST_REQUEST
            })
        ).toEqual(
            {
                isFetching: true,
                isFetched: false,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: null
            }
        )
    })
    it('should handle MOVIE_LIST_SUCCESS', () => {
        expect(
            movieListReducer({
                isFetching: true,
                isFetched: false,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: null}, {
                type: movieListConstants.MOVIE_LIST_SUCCESS
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: undefined
            }
        )
    })
    it('should handle MOVIE_LIST_FAILURE', () => {
        expect(
            movieListReducer({
                isFetching: true,
                isFetched: false,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: null}, {
                type: movieListConstants.MOVIE_LIST_FAILURE
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: false,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: null
            }
        )
    })
    it('should handle MOVIE_LIST_FILTERS_UPDATE', () => {
        expect(
            movieListReducer({
                isFetching: false,
                isFetched: true,
                filters: {dateRange: {beginDate: 0, endDate: 0}},
                playedMovies: undefined}, {
                type: movieListConstants.MOVIE_LIST_FILTERS_UPDATE
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                filters: undefined,
                playedMovies: undefined
            }
        )
    })
})