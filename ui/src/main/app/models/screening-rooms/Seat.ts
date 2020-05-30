import {ReservationState} from "./ReservationState";

export interface SeatInterface {
    id: number;
    seatNumber: number;
    rowNumber: number;
    reservationState: ReservationState;
}