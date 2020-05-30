import {Screening} from "../../models/screening";
import {
    ScreeningsAction,
    ScreeningsSetCurrentActionInterface,
    ScreeningsSuccessActionInterface
} from "../actions/screening";
import {screeningConstants} from "../constants/screening-constants";

export interface ScreeningsState {
    screenings: Screening[];
    currentScreening?: Screening;
    isFetching: boolean;
    isFetched: boolean;
}

const initialState: ScreeningsState = {
    screenings: [],
    isFetching: false,
    isFetched: false
}

export function screeningReducer(state: ScreeningsState = initialState, action: ScreeningsAction): ScreeningsState {
    switch (action.type) {
        case screeningConstants.MOVIE_SCREENINGS_REQUEST:
            return {
                ...state,
                isFetched: false,
                isFetching: true
            }
        case screeningConstants.MOVIE_SCREENINGS_FAILURE:
            return initialState
        case screeningConstants.MOVIE_SCREENINGS_SUCCESS:
            const screenings = (action as ScreeningsSuccessActionInterface).screenings;
            return {
                ...state,
                isFetched: true,
                isFetching: false,
                screenings: screenings,
                currentScreening: screenings.length > 0 ? screenings[0] : undefined
            }
        case screeningConstants.SET_CURRENT_SCREENING:
            const screeningId = (action as ScreeningsSetCurrentActionInterface).screeningId;
            const screening: Screening | undefined =
                state.screenings.filter(screening => screening.screeningId === screeningId)[0];
            return {
                ...state,
                currentScreening: screening
            }
        default:
            return state;
    }
}