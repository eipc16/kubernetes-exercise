import {seatsReducer} from "../seats-reducer";
import {seatConstants} from "../../constants/seat-constants";
import {ReservationState} from "../../../models/screening-rooms";

describe('Seats reducer', () => {
    it('should handle SEATS_LAYOUT_REQUEST', () => {
        expect(
            seatsReducer({seats: {}, isFetching: false, isFetched: false}, {
                type: seatConstants.SEAT_LAYOUT_REQUEST,
                screeningId: 0
            })
        ).toEqual(
            {
                isFetched: false,
                isFetching: true,
                seats: {}
            }
        )
    })
    it('should handle SEATS_LAYOUT_FAILURE', () => {
        expect(
            seatsReducer({seats: {}, isFetching: true, isFetched: false}, {
                type: seatConstants.SEAT_LAYOUT_FAILURE,
                error: "Error"
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: false,
                seats: {}
            }
        )
    })
    it('should handle SEATS_LAYOUT_SUCCESS', () => {
        expect(
            seatsReducer({seats: {}, isFetching: true, isFetched: false}, {
                type: seatConstants.SEAT_LAYOUT_SUCCESS, seats: {}
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                seats: {}
            }
        )
    })
    it('should handle UPDATE_SEAT_STATE', () => {
        expect(
            seatsReducer({isFetching: false, isFetched: true,
                seats: {[0]: {seatNumber: 0, id: 0, reservationState: ReservationState.AVAILABLE, rowNumber: 0}}}, {
                type: seatConstants.UPDATE_SEAT_STATE, seatId: 0, reservationState: ReservationState.SELECTED
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: true,
                seats: {[0]: {seatNumber: 0, id: 0, reservationState: ReservationState.SELECTED, rowNumber: 0}}
            }
        )
    })
})