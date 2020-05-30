import {Action} from "redux";
import {screeningConstants} from "../../constants/screening-constants";
import {Screening} from "../../../models/screening";

export interface ScreeningsRequestActionInterface extends Action {
    type: typeof screeningConstants.MOVIE_SCREENINGS_REQUEST,
    movieId: number;
}

export interface ScreeningsFailureActionInterface extends Action {
    type: typeof screeningConstants.MOVIE_SCREENINGS_SUCCESS,
    error: string;
}

export interface ScreeningsSuccessActionInterface extends Action {
    type: typeof screeningConstants.MOVIE_SCREENINGS_FAILURE
    screenings: Screening[];
}

export interface ScreeningsSetCurrentActionInterface extends Action {
    type: typeof screeningConstants.SET_CURRENT_SCREENING,
    screeningId: number;
}

export type ScreeningsAction = ScreeningsRequestActionInterface |
    ScreeningsFailureActionInterface |
    ScreeningsSuccessActionInterface |
    ScreeningsSetCurrentActionInterface;
