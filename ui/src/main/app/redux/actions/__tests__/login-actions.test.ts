import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'
import fetchMock from 'fetch-mock'
import expect from 'expect'
import {appConfig} from "../../../config";
import {loginConstants} from "../../constants";
import {AuthenticationServiceImpl} from "../../../services";
import {LoginActionPublisherImpl} from "../login";
import {LoginData} from "../../../models/authorization";
const middlewares = [thunk]
const mockStore = configureMockStore(middlewares)

describe('Login actions test', () => {
    afterEach(() => {
        fetchMock.reset()
    })
    it('Test action login success', () => {
        const token = { accessToken: "aaaaa", tokenType: "Bearer"}
        const userData: LoginData = { usernameOrEmail: "", password: "", remember: true}
        fetchMock.postOnce(`${appConfig.apiUrl}/auth/signin`,{
            body: { ...token },
            headers: { 'content-type': 'application/json' }
        })

        const authAction = new LoginActionPublisherImpl(AuthenticationServiceImpl.createInstance())
        const expectedActions = [
            { type: loginConstants.LOGIN_REQUEST, userData: userData},
            { type: loginConstants.LOGIN_SUCCESS, token: token }
        ]
        const store = mockStore({})
        // @ts-ignore
        store.dispatch(authAction.login(userData))
        return store.getActions().forEach(action => expect(expectedActions.includes(action)
            && action.length === expectedActions.length));
    })
    it('Test action login failure', () => {
        const error = "Internal Server Error"
        const userData: LoginData = { usernameOrEmail: "", password: "", remember: true}
        fetchMock.postOnce(`${appConfig.apiUrl}/auth/signin`, {
            body: { error },
            headers: { 'content-type': 'application/json' },
            status: 500
        })

        const authAction = new LoginActionPublisherImpl(AuthenticationServiceImpl.createInstance())
        const expectedActions = [
            { type: loginConstants.LOGIN_REQUEST, userData: userData },
            { type: loginConstants.LOGIN_FAILURE, error: "Internal Server Error"}
        ]
        const store = mockStore({})
        // @ts-ignore
        store.dispatch(authAction.login(userData))
        return store.getActions().forEach(action => expect(expectedActions.includes(action)
            && action.length === expectedActions.length));
    })
});
