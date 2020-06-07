import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'
import fetchMock from 'fetch-mock'
import expect from 'expect'
import {appConfig} from "../../../config";
import {GenreActionPublisherImpl} from "../genre";
import {GenreServiceImpl} from "../../../services/genre-service";
import {genreConstants} from "../../constants";
import {MockAuthenticationService} from "../../../utils/mock-authentication-service";
const middlewares = [thunk]
const mockStore = configureMockStore(middlewares)

describe('Genre actions test', () => {
    afterEach(() => {
        fetchMock.reset()
    })

    it('test action fetchGenres success', () => {
        const genreList = { 0: {name: "", id: 0}}
        fetchMock.getOnce(`${appConfig.apiUrl}/genres?searchText=thriller`, {
            body: { ...genreList },
            headers: { 'content-type': 'application/json' }
        })

        const authService = new MockAuthenticationService();
        const actions = new GenreActionPublisherImpl(GenreServiceImpl.createInstance(authService))
        const expectedActions = [
            { type: genreConstants.GENRES_REQUEST },
            { type: genreConstants.GENRES_SUCCESS, genreList: genreList }
        ]
        const store = mockStore({})
        // @ts-ignore
        store.dispatch(actions.fetchGenres("thriller"))
        return store.getActions().forEach(action => expect(expectedActions.includes(action)
            && action.length === expectedActions.length));
    })
    it('test action fetchGenres failure', () => {
        const error = "Internal Server Error"
        fetchMock.getOnce(`${appConfig.apiUrl}/genres?searchText=thriller`, {
            body: { error },
            headers: { 'content-type': 'application/json' },
            status: 500
        })
        const authService = new MockAuthenticationService();
        const actions = new GenreActionPublisherImpl(GenreServiceImpl.createInstance(authService))
        const expectedActions = [
            { type: genreConstants.GENRES_REQUEST },
            { type: genreConstants.GENRES_FAILURE, error: "Internal Server Error"}
        ]
        const store = mockStore({})
        // @ts-ignore
        store.dispatch(actions.fetchGenres("thriller"))
        return store.getActions().forEach(action => expect(expectedActions.includes(action)
            && action.length === expectedActions.length));
    })
})