import {Action, Dispatch} from "redux";
import {Alert} from "../../../models/infrastructure";
import {
    ScreeningsFailureActionInterface,
    ScreeningsRequestActionInterface, ScreeningsSetCurrentActionInterface,
    ScreeningsSuccessActionInterface
} from "./types";
import {AlertPublisher, AlertPublisherImpl} from "../alert";
import {ScreeningService} from "../../../services/screening-service";
import {screeningConstants} from "../../constants/screening-constants";
import {Screening} from "../../../models/screening";
import {SeatActionPublisher, SeatActionPublisherImpl} from "../seat";
import {SeatServiceImpl} from "../../../services/seat-service";
import moment from "moment";

export interface ScreeningActionPublisher {
    fetchScreenings(movieId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void;
    setCurrentScreening(screeningId: number): (dispatch: Dispatch<Action>) => void;
}

export class ScreeningActionPublisherImpl implements ScreeningActionPublisher {
    private screeningService: ScreeningService;
    private alertPublisher: AlertPublisher;
    private seatsPublisher: SeatActionPublisher;

    constructor(screeningService: ScreeningService, seatsPublisher?: SeatActionPublisher, alertPublisher?: AlertPublisher) {
        this.screeningService = screeningService;
        this.seatsPublisher = seatsPublisher || new SeatActionPublisherImpl(SeatServiceImpl.createInstance());
        this.alertPublisher = alertPublisher || AlertPublisherImpl.createInstance();
    }

    fetchScreenings(movieId: number, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void {
        function request(movieId: number): ScreeningsRequestActionInterface {
            return {
                type: screeningConstants.MOVIE_SCREENINGS_REQUEST,
                movieId: movieId
            }
        }

        function success(screenings: Screening[]): ScreeningsSuccessActionInterface {
            return {
                type: screeningConstants.MOVIE_SCREENINGS_SUCCESS,
                screenings: screenings
            }
        }

        function failure(error: string): ScreeningsFailureActionInterface {
            return {
                type: screeningConstants.MOVIE_SCREENINGS_FAILURE,
                error: error
            }
        }

        return (dispatch: Dispatch<Action>): void => {
            dispatch(request(movieId));

            this.screeningService.fetchScreenings(movieId)
                .then((screenings: Screening[]) => screenings
                    .filter(screening => moment(screening.startTime).isAfter(moment.now())))
                .then(
                    (screenings: Screening[]) => {
                        dispatch(success(screenings));
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

    setCurrentScreening(screeningId: number): (dispatch: Dispatch<Action>) => void {
        function setScreening(screeningId: number): ScreeningsSetCurrentActionInterface {
            return {
                type: screeningConstants.SET_CURRENT_SCREENING,
                screeningId: screeningId
            };
        }

        return (dispatch: Dispatch<Action>): void => {
            this.seatsPublisher.fetchSeats(screeningId)(dispatch);
            dispatch(setScreening(screeningId));
        }
    }
}