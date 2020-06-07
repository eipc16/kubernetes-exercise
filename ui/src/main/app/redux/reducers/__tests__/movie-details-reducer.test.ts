import {movieDetailsConstants} from '../../constants'
import {movieDetailsReducer} from "../movie-details";
import {MovieDetails} from "../../../models/movie-details";
import {ObjectState} from "../../../models/infrastructure";

describe('Movie details reducer', () => {
    it('should handle MOVIE_DETAILS_REQUEST', () => {
        expect(
            movieDetailsReducer({isFetched: false, isFetching: false, movie: null}, {
                type: movieDetailsConstants.MOVIE_DETAILS_REQUEST, id: 1
            })
        ).toEqual(
            {
                isFetching: true,
                isFetched: false,
                movie: null
            }
        )
    })
    it('should handle MOVIE_DETAILS_FAILURE', () => {
        expect(
            movieDetailsReducer({isFetched: false, isFetching: true, movie: null}, {
                type: movieDetailsConstants.MOVIE_DETAILS_FAILURE, error: "Error"
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: false,
                movie: null
            }
        )
    })
    it('should handle MOVIE_DETAILS_SUCCESS', () => {
        const list: MovieDetails[] = [{actors: "", runtime: "0", plot: "", id: 0, director: "", year: "2020",
            posterUrl: "", country: "", language: "", title: "", objectState: ObjectState.ACTIVE,
            releaseDate: Date.prototype, imdbId: "0", maturityRate: "0", genres: []}];
        expect(
            movieDetailsReducer({isFetched: true, isFetching: false, movie: null}, {
                type: movieDetailsConstants.MOVIE_DETAILS_SUCCESS,
                movie: {list: list}
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                movie: {list: list}
            }
        )
    })
})