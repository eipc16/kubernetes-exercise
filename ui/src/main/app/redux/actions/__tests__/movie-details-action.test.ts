import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'
import fetchMock from 'fetch-mock'
import expect from 'expect'
import {movieDetailsConstants} from "../../constants";
import {ObjectState} from "../../../models/infrastructure";
import {MovieDetailsActionPublisherImpl} from "../movie-details";
import {MovieDetailsServiceImpl} from "../../../services/movie-details-service";
import {appConfig} from "../../../config";
const middlewares = [thunk]
const mockStore = configureMockStore(middlewares)

describe('Movie details actions tests', () => {
    afterEach(() => {
        fetchMock.reset()
    })

    it('test action getMovieDetails success', () => {
        const movieBody = { actors: "", runtime: "0", plot: "", id: 0, director: "", year: "2020",
            posterUrl: "", country: "", language: "", title: "", objectState: ObjectState.ACTIVE,
            releaseDate: new Date(Date.now()).toUTCString(), imdbId: "0", maturityRate: "0", genres: [] }

        fetchMock.getOnce(`${appConfig.apiUrl}/movies/0/details`, {
            body: { ...movieBody },
            headers: { 'content-type': 'application/json' }
        })

        const actions = new MovieDetailsActionPublisherImpl(MovieDetailsServiceImpl.createInstance())
        const expectedActions = [
            { type: movieDetailsConstants.MOVIE_DETAILS_REQUEST, id: 0 },
            { type: movieDetailsConstants.MOVIE_DETAILS_SUCCESS, movie: movieBody }
        ]
        const store = mockStore({ movie: movieBody })
        // @ts-ignore
        return store.dispatch(actions.getMovieDetails(0)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    })
    it('test action getMovieDetails failure', () => {
        const error = "Internal Server Error"
        fetchMock.getOnce(`${appConfig.apiUrl}/movies/0/details`, {
            body: { error },
            headers: { 'content-type': 'application/json' },
            status: 500
        })

        const actions = new MovieDetailsActionPublisherImpl(MovieDetailsServiceImpl.createInstance())
        const expectedActions = [
            { type: movieDetailsConstants.MOVIE_DETAILS_REQUEST, id: 0 },
            { type: movieDetailsConstants.MOVIE_DETAILS_FAILURE, error: "Internal Server Error"}
        ]
        const store = mockStore({ error: error })
        // @ts-ignore
        return store.dispatch(actions.getMovieDetails(0)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    })
})