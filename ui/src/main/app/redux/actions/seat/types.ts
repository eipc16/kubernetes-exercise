import {Action} from "redux";
import {seatConstants} from "../../constants/seat-constants";
import {ReservationState, Seat} from "../../../models/screening-rooms";

export interface SeatsMap {
    [seatId: number]: Seat;
}

export interface SeatFailureActionInterface extends Action {
    type: typeof seatConstants.SEAT_LAYOUT_FAILURE,
    error: string;
}

export interface SeatSuccessActionInterface extends Action {
    type: typeof seatConstants.SEAT_LAYOUT_SUCCESS,
    seats: SeatsMap;
}

export interface SeatRequestActionInterface extends Action {
    type: typeof seatConstants.SEAT_LAYOUT_REQUEST,
    screeningId: number;
}

export interface SeatUpdateStateActionInterface extends Action {
    type: typeof seatConstants.UPDATE_SEAT_STATE,
    seatId: number,
    reservationState: ReservationState
}

export type SeatAction = SeatFailureActionInterface | SeatSuccessActionInterface
                        | SeatRequestActionInterface | SeatUpdateStateActionInterface
