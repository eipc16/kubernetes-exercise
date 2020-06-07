import {Action, Dispatch} from "redux";
import {Alert, Resource} from "../../../models/infrastructure";
import {ReservationState, Seat} from "../../../models/screening-rooms";
import {
    SeatFailureActionInterface,
    SeatRequestActionInterface,
    SeatReservationFailureActionInterface,
    SeatReservationRequestActionInterface,
    SeatReservationSuccessActionInterface,
    SeatSuccessActionInterface,
    SeatUpdateStateActionInterface
} from "./types";
import {seatConstants} from "../../constants/seat-constants";
import {SeatService} from "../../../services/seat-service";
import {AlertPublisher, AlertPublisherImpl} from "../alert";
import {Reservation} from "../../../models/reservation";
import {SeatInterface} from "../../../models/screening-rooms/Seat";

export interface SeatActionPublisher {
    fetchSeats(screeningId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => Promise<SeatInterface[] | void>;

    updateSeatReservationState(seatId: number, seatReservationState: ReservationState): SeatUpdateStateActionInterface;

    reserveSeats(reservation: Reservation, errorAlertSupplier?: (message: string) => Alert,
                 onSuccess?: (resource: Resource) => void, onException?: (errorResponse: string) => void): (dispatch: Dispatch<Action>) => Promise<Resource | void>;
}

export class SeatActionPublisherImpl implements SeatActionPublisher {
    private seatService: SeatService;
    private alertPublisher: AlertPublisher;

    constructor(seatService: SeatService, alertPublisher?: AlertPublisher) {
        this.seatService = seatService;
        this.alertPublisher = alertPublisher || AlertPublisherImpl.createInstance();
    }

    fetchSeats(screeningId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => Promise<SeatInterface[] | void> {
        function request(screeningId: number): SeatRequestActionInterface {
            return {
                type: seatConstants.SEAT_LAYOUT_REQUEST,
                screeningId: screeningId
            }
        }

        function success(seats: Seat[]): SeatSuccessActionInterface {
            const seatsMap = Object.values(seats).reduce((acc: { [seatId: number]: Seat }, seat: Seat) => {
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

        return (dispatch: (Dispatch<Action>)): Promise<Seat[] | void> => {
            dispatch(request(screeningId));

            return this.seatService.fetchSeats(screeningId)
                .then(
                    (seats: Seat[]) => {
                        dispatch(success(seats))
                    },
                    (errorResponse: string) => {
                        dispatch(failure(errorResponse));
                        if (errorAlertSupplier) {
                            const alert = errorAlertSupplier(errorResponse);
                            this.alertPublisher.pushAlert(alert)(dispatch);
                        }
                    }
                )
        };
    }

    updateSeatReservationState(seatId: number, seatReservationState: ReservationState): SeatUpdateStateActionInterface {
        return {
            type: seatConstants.UPDATE_SEAT_STATE,
            seatId: seatId,
            reservationState: seatReservationState
        };
    }

    reserveSeats(reservation: Reservation, errorAlertSupplier?: (message: string) => Alert,
                 onSuccess?: (resource: Resource) => void, onException?: (errorResponse: string) => void): (dispatch: Dispatch<Action>) => Promise<Resource | void> {
        function request(reservation: Reservation): SeatReservationRequestActionInterface {
            return {
                type: seatConstants.SEAT_RESERVATION_REQUEST,
                reservation: reservation
            }
        }

        function success(resource: Resource): SeatReservationSuccessActionInterface {
            return {
                type: seatConstants.SEAT_RESERVATION_SUCCESS,
                resource: resource
            }
        }

        function failure(error: string): SeatReservationFailureActionInterface {
            return {
                type: seatConstants.SEAT_RESERVATION_FAILURE,
                error: error
            }
        }

        return (dispatch: Dispatch<Action>): Promise<Resource | void> => {
            dispatch(request(reservation));
            return this.seatService.reserveSeats(reservation)
                .then(
                    (resource: Resource) => {
                        dispatch(success(resource));
                        if (onSuccess) {
                            onSuccess(resource);
                        }
                    },
                    (errorResponse: string) => {
                        dispatch(failure(errorResponse));
                        if (errorAlertSupplier) {
                            const alert = errorAlertSupplier(errorResponse);
                            this.alertPublisher.pushAlert(alert)(dispatch);
                        }
                        if (onException) {
                            onException(errorResponse);
                        }
                    }
                )
        };
    }
}