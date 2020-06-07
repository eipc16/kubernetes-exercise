import fetchMock from "fetch-mock";
import {appConfig} from "../../../config";
import expect from "expect";
import {ReservationState} from "../../../models/screening-rooms";
import {SeatsMap} from "../seat";
import {MockAuthenticationService} from "./utils/mock-authentication-service";
import {seatConstants} from "../../constants/seat-constants";
import thunk from "redux-thunk";
import configureMockStore from "redux-mock-store";
import {ObjectState} from "../../../models/infrastructure";
import {ScreeningServiceImpl} from "../../../services/screening-service";
import {ScreeningActionPublisherImpl} from "../screening";
import {Screening} from "../../../models/screening";
import {screeningConstants} from "../../constants/screening-constants";
import moment from 'moment';
import {act} from "react-dom/test-utils";

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

describe('Screening actions tests', () => {
    afterEach(() => {
        fetchMock.reset()
    });

    const authService = new MockAuthenticationService();
    const screeningService = ScreeningServiceImpl.createInstance(authService);
    const screeningPublisher = new ScreeningActionPublisherImpl(screeningService);

    it('test action fetchScreenings success', () => {
        const screenings: Screening[] = [
            {
                screeningId: 0,
                movieId: 6,
                screeningRoomId: 5,
                screeningRoom: {
                    id: 5,
                    number: 6,
                    rowsNumber: 1,
                    seatsInRowNumber: 5,
                    objectState: ObjectState.ACTIVE
                },
                price: 2.42,
                startTime: moment(moment.now()).add(1, 'days').toDate(),
                endTime: moment(moment.now()).add(1, 'days').add(2, 'hours').toDate(),
                objectState: ObjectState.ACTIVE
            },
            {
                screeningId: 3,
                movieId: 6,
                screeningRoomId: 6,
                screeningRoom: {
                    id: 5,
                    number: 6,
                    rowsNumber: 1,
                    seatsInRowNumber: 5,
                    objectState: ObjectState.ACTIVE
                },
                price: 3.42,
                startTime: moment(moment.now()).add(1, 'days').toDate(),
                endTime: moment(moment.now()).add(1, 'days').add(2, 'hours').toDate(),
                objectState: ObjectState.ACTIVE
            },
        ];

        const movieId = 6;

        fetchMock.getOnce(`${appConfig.apiUrl}/screening/movie/${movieId}`, {
            body: [...screenings],
            headers: {'content-type': 'application/json'}
        });

        const expectedActions = [
            {type: screeningConstants.MOVIE_SCREENINGS_REQUEST, movieId: movieId},
            {type: screeningConstants.MOVIE_SCREENINGS_SUCCESS, screenings: screenings}
        ];

        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(screeningPublisher.fetchScreenings(movieId)).then(() => {
            return expect(expectedActions.length).toEqual(store.getActions().length) && expectedActions.map(action => {
                store.getActions().includes(action);
            });
        });
    });
    it('test action fetchScreenings failure', () => {
        const error = "Precondition Failed";
        const movieId = 6;

        fetchMock.getOnce(`${appConfig.apiUrl}/screening/movie/${movieId}`, {
            body: {error},
            headers: {'content-type': 'application/json'},
            status: 412
        });
        const expectedActions = [
            {type: screeningConstants.MOVIE_SCREENINGS_REQUEST, movieId: movieId},
            {type: screeningConstants.MOVIE_SCREENINGS_FAILURE, error: error}
        ];
        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(screeningPublisher.fetchScreenings(movieId)).then(() => {
            return expect(store.getActions()).toEqual(expectedActions);
        });
    });
    it('test action updateSeatReservationState', () => {
        const seatsMap: SeatsMap = {
            0: {
                id: 0,
                seatNumber: 1,
                rowNumber: 1,
                reservationState: ReservationState.AVAILABLE
            },
            1: {
                id: 1,
                seatNumber: 2,
                rowNumber: 1,
                reservationState: ReservationState.RESERVED
            }
        };
        const screeningId = 0;

        fetchMock.getOnce(`${appConfig.apiUrl}/seats/screening/${screeningId}`, {
            body: {...Object.values(seatsMap)},
            headers: {'content-type': 'application/json'}
        });

        const expectedActions = [
            {type: seatConstants.SEAT_LAYOUT_REQUEST, screeningId: screeningId},
            {type: screeningConstants.SET_CURRENT_SCREENING, screeningId: screeningId}
        ];

        const store = mockStore({});
        // @ts-ignore
        store.dispatch(screeningPublisher.setCurrentScreening(screeningId));
        return expect(store.getActions().length).toEqual(expectedActions.length) && store.getActions()
            .forEach(action => expect(expectedActions.includes(action)));
    });
});