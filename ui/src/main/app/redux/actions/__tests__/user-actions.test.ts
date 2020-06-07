import configureMockStore from 'redux-mock-store'
import thunk from 'redux-thunk'
import fetchMock from 'fetch-mock'
import expect from 'expect'
import {userConstants} from "../../constants";
import {appConfig} from "../../../config";
import {User} from "../../../models/users";
import {UserActionPublisherImpl} from "../user";
import {UserServiceImpl} from "../../../services";

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

describe('User actions tests', () => {
    afterEach(() => {
        fetchMock.reset()
    });

    it('test action getCurrentUser success', () => {
        const user: User = {
            id: 0,
            name: 'Adam',
            surname: 'Nowak',
            username: 'adam.nowak',
            password: '',
            email: 'email',
            phoneNumber: '123321123',
            role: 'USER'
        };
        fetchMock.getOnce(`${appConfig.apiUrl}/user/current`, {
            body: {...user},
            headers: {'content-type': 'application/json'}
        });

        const actions = new UserActionPublisherImpl(new UserServiceImpl());
        const expectedActions = [
            {type: userConstants.CURRENT_USER_REQUEST},
            {type: userConstants.CURRENT_USER_SUCCESS, userData: user}
        ];
        const store = mockStore({currentUser: user});
        // @ts-ignore
        return store.dispatch(actions.getCurrentUser()).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    });
    it('test action getCurrentUser failure', () => {
        const error = "Precondition Failed";
        fetchMock.getOnce(`${appConfig.apiUrl}/user/current`, {
            body: {error},
            headers: {'content-type': 'application/json'},
            status: 412
        });

        const actions = new UserActionPublisherImpl(new UserServiceImpl());
        const expectedActions = [
            {type: userConstants.CURRENT_USER_REQUEST},
            {type: userConstants.CURRENT_USER_FAILURE, error: error}
        ];
        const store = mockStore({error: error});
        // @ts-ignore
        return store.dispatch(actions.getCurrentUser()).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    })
});