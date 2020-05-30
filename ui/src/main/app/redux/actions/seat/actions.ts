import {Action, Dispatch} from "redux";
import {Alert} from "../../../models/infrastructure";
import {ReservationState, Seat} from "../../../models/screening-rooms";
import {
    SeatFailureActionInterface,
    SeatRequestActionInterface,
    SeatSuccessActionInterface,
    SeatUpdateStateActionInterface
} from "./types";
import {seatConstants} from "../../constants/seat-constants";
import {SeatService} from "../../../services/seat-service";
import {AlertPublisher, AlertPublisherImpl} from "../alert";

export interface SeatActionPublisher {
    fetchSeats(screeningId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void;
    updateSeatReservationState(seatId: number, seatReservationState: ReservationState): SeatUpdateStateActionInterface;
}

export class SeatActionPublisherImpl implements SeatActionPublisher {
    private seatService: SeatService;
    private alertPublisher: AlertPublisher;

    constructor(seatService: SeatService, alertPublisher?: AlertPublisher) {
        this.seatService = seatService;
        this.alertPublisher = alertPublisher || AlertPublisherImpl.createInstance();
    }

    fetchSeats(screeningId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void {
        return (dispatch: Dispatch<Action>) => {
            dispatch(request(screeningId));

            this.seatService.fetchSeats(screeningId)
                .then(
                    (seats: Seat[]) => {
                        dispatch(success(seats))
                    },
                    (errorResponse: string) => {
                        dispatch(failure(errorResponse))
                        if (errorAlertSupplier) {
                            const alert = errorAlertSupplier(errorResponse)
                            this.alertPublisher.pushAlert(alert)(dispatch)
                        }
                    }
                )
        }

        function request(screeningId: number): SeatRequestActionInterface {
            return {
                type: seatConstants.SEAT_LAYOUT_REQUEST,
                screeningId: screeningId
            }
        }

        function success(seats: Seat[]): SeatSuccessActionInterface {
            const seatsMap = seats.reduce((acc: { [seatId: number]: Seat }, seat: Seat) => {
                return {
                    ...acc,
                    [seat.id]: seat
                }
            }, {});
            return {
                type: seatConstants.SEAT_LAYOUT_SUCCESS,
                seats: seatsMap
            }
        }

        function failure(error: string): SeatFailureActionInterface {
            return {
                type: seatConstants.SEAT_LAYOUT_FAILURE,
                error: error
            }
        }
    }

    updateSeatReservationState(seatId: number, seatReservationState: ReservationState): SeatUpdateStateActionInterface {
        return {
            type: seatConstants.UPDATE_SEAT_STATE,
            seatId: seatId,
            reservationState: seatReservationState
        };
    }
}