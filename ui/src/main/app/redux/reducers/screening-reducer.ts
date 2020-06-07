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
};

function saveFetchedScreenings(state: ScreeningsState, action: ScreeningsSuccessActionInterface): ScreeningsState {
    const {screenings} = action;
    return {
        ...state,
        isFetched: true,
        isFetching: false,
        screenings: screenings,
        currentScreening: screenings.length > 0 ? screenings[0] : undefined
    };
}

function setCurrentScreening(state: ScreeningsState, action: ScreeningsSetCurrentActionInterface): ScreeningsState {
    const screeningId = (action as ScreeningsSetCurrentActionInterface).screeningId;
    const screening: Screening | undefined =
        state.screenings.filter(screening => screening.screeningId === screeningId)[0];
    return {
        ...state,
        currentScreening: screening
    };
}

export function screeningReducer(state: ScreeningsState = initialState, action: ScreeningsAction): ScreeningsState {
    switch (action.type) {
        case screeningConstants.MOVIE_SCREENINGS_REQUEST:
            return {
                ...state,
                isFetched: false,
                isFetching: true
            };
        case screeningConstants.MOVIE_SCREENINGS_FAILURE:
            return initialState;
        case screeningConstants.MOVIE_SCREENINGS_SUCCESS:
            return saveFetchedScreenings(state, (action as ScreeningsSuccessActionInterface));
        case screeningConstants.SET_CURRENT_SCREENING:
            return setCurrentScreening(state, (action as ScreeningsSetCurrentActionInterface));
        default:
            return state;
    }
}