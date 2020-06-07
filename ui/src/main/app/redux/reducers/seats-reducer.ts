import {SeatAction, SeatsMap, SeatSuccessActionInterface, SeatUpdateStateActionInterface} from "../actions/seat";
import {seatConstants} from "../constants/seat-constants";

export interface SeatsState {
    seats: SeatsMap;
    screeningId?: number;
    isFetching: boolean;
    isFetched: boolean;
}

const initialState: SeatsState = {
    seats: {},
    isFetching: false,
    isFetched: false
};

function updateSeatState(state: SeatsState, action: SeatUpdateStateActionInterface): SeatsState {
    const { seatId, reservationState } = action;
    return {
        ...state,
        seats: {
            ...state.seats,
            [seatId]: {
                ...state.seats[seatId],
                reservationState: reservationState
            }
        }
    };
}

export function seatsReducer(state: SeatsState = initialState, action: SeatAction): SeatsState {
    switch (action.type) {
        case seatConstants.SEAT_LAYOUT_REQUEST:
            return {
                ...state,
                isFetched: false,
                isFetching: true
            };
        case seatConstants.SEAT_LAYOUT_FAILURE:
            return initialState;
        case seatConstants.SEAT_LAYOUT_SUCCESS:
            return {
                ...state,
                isFetched: true,
                isFetching: false,
                seats: (action as SeatSuccessActionInterface).seats
            };
        case seatConstants.UPDATE_SEAT_STATE:
            return updateSeatState(state, (action as SeatUpdateStateActionInterface));
        default:
            return state;
    }
}