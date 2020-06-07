import fetchMock from "fetch-mock";
import {appConfig} from "../../../config";
import expect from "expect";
import {ReservationState} from "../../../models/screening-rooms";
import {SeatActionPublisherImpl, SeatsMap, SeatUpdateStateActionInterface} from "../seat";
import {MockAuthenticationService} from "../../../utils/mock-authentication-service";
import {SeatServiceImpl} from "../../../services/seat-service";
import {seatConstants} from "../../constants/seat-constants";
import thunk from "redux-thunk";
import configureMockStore from "redux-mock-store";
import {Reservation} from "../../../models/reservation";
import {Resource} from "../../../models/infrastructure";

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

describe('Seat actions tests', () => {
    afterEach(() => {
        fetchMock.reset()
    });

    const authService = new MockAuthenticationService();
    const seatService = SeatServiceImpl.createInstance(authService);
    const seatPublisher = new SeatActionPublisherImpl(seatService);

    it('test action fetchSeats success', () => {
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
            {type: seatConstants.SEAT_LAYOUT_SUCCESS, seats: seatsMap}
        ];
        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(seatPublisher.fetchSeats(screeningId)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    });
    it('test action fetchSeats failure', () => {
        const error = "Precondition Failed";
        const screeningId = 0;
        fetchMock.getOnce(`${appConfig.apiUrl}/seats/screening/${screeningId}`, {
            body: {error},
            headers: {'content-type': 'application/json'},
            status: 412
        });

        const expectedActions = [
            {type: seatConstants.SEAT_LAYOUT_REQUEST, screeningId: screeningId},
            {type: seatConstants.SEAT_LAYOUT_FAILURE, error: error}
        ];
        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(seatPublisher.fetchSeats(screeningId)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    });
    it('test action reserveSeats success', () => {
        const reservation: Reservation = {
            screeningId: 0,
            seatsIds: [1, 2, 3, 4, 5]
        };
        const resource: Resource = {
            id: 32,
            identifier: "identifier",
            uri: "path"
        };

        fetchMock.postOnce(`${appConfig.apiUrl}/reservation`, {
            body: {...resource},
            headers: {'content-type': 'application/json'}
        });

        const expectedActions = [
            {type: seatConstants.SEAT_RESERVATION_REQUEST, reservation: reservation},
            {type: seatConstants.SEAT_RESERVATION_SUCCESS, resource: resource}
        ];
        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(seatPublisher.reserveSeats(reservation)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    });
    it('test action reserveSeats failure', () => {
        const error = "Precondition Failed";
        const reservation: Reservation = {
            screeningId: 0,
            seatsIds: [1, 2, 3, 4, 5]
        };
        fetchMock.postOnce(`${appConfig.apiUrl}/reservation`, {
            body: {error},
            headers: {'content-type': 'application/json'},
            status: 412
        });
        const expectedActions = [
            {type: seatConstants.SEAT_RESERVATION_REQUEST, reservation: reservation},
            {type: seatConstants.SEAT_RESERVATION_FAILURE, error: error}
        ];
        const store = mockStore({});
        // @ts-ignore
        return store.dispatch(seatPublisher.reserveSeats(reservation)).then(() => {
            expect(store.getActions()).toEqual(expectedActions)
        })
    });
    it('test action updateSeatReservationState', () => {
        const seatId = 5;
        const seatReservationState = ReservationState.AVAILABLE;

        const expectedActions = [
            {
                type: seatConstants.UPDATE_SEAT_STATE,
                seatId: seatId,
                reservationState: seatReservationState
            }
        ];

        const store = mockStore({});
        // @ts-ignore
        store.dispatch(seatPublisher.updateSeatReservationState(seatId, seatReservationState));
        return expect(store.getActions()).toEqual(expectedActions);
    });
});