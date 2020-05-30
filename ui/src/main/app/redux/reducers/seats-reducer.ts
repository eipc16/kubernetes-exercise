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
}

export function seatsReducer(state: SeatsState = initialState, action: SeatAction): SeatsState {
    switch (action.type) {
        case seatConstants.SEAT_LAYOUT_REQUEST:
            return {
                ...state,
                isFetched: false,
                isFetching: true
            }
        case seatConstants.SEAT_LAYOUT_FAILURE:
            return initialState
        case seatConstants.SEAT_LAYOUT_SUCCESS:
            return {
                ...state,
                isFetched: true,
                isFetching: false,
                seats: (action as SeatSuccessActionInterface).seats
            }
        case seatConstants.UPDATE_SEAT_STATE:
            const { seatId, reservationState } = (action as SeatUpdateStateActionInterface);
            return {
                ...state,
                seats: {
                    ...state.seats,
                    [seatId]: {
                        ...state.seats[seatId],
                        reservationState: reservationState
                    }
                }
            }
        default:
            return state;
    }
}