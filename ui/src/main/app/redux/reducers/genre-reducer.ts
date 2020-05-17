import {Genre} from "../../models/genre";
import {GenreAction, GenreSuccessActionInterface} from "../actions/genre/types";
import {genreConstants} from "../constants";

const initialState: GenreState = {
    isFetching: true,
    isFetched: false,
    genreList: [],
    totalGenres: 0
}

export interface GenreState {
    isFetching: boolean,
    isFetched: boolean,
    genreList: Genre[],
    totalGenres: number
}

export function genreReducer(state: GenreState = initialState, action: GenreAction): GenreState {
    switch (action.type) {
        case genreConstants.GENRES_REQUEST:
            return {
                isFetching: true,
                isFetched: false,
                genreList: [],
                totalGenres: 0
            }
        case genreConstants.GENRES_SUCCESS:
            return {
                isFetching: false,
                isFetched: true,
                genreList: (action as GenreSuccessActionInterface).genreList,
                totalGenres: (action as GenreSuccessActionInterface).genreList.length
            }
        case genreConstants.GENRES_FAILURE:
            return initialState
        default:
            return state;
    }
}