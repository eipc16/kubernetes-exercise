import {Action} from "redux";
import {genreConstants} from "../../constants";
import {Genre} from "../../../models/genre";

export interface GenreRequestActionInterface extends Action {
    type: typeof genreConstants.GENRES_REQUEST;
}

export interface GenreFailureActionInterface extends Action {
    type: typeof genreConstants.GENRES_FAILURE;
    error: string;
}

export interface GenreSuccessActionInterface extends Action {
    type: typeof genreConstants.GENRES_SUCCESS;
    genreList: Genre[];
}

export type GenreAction = GenreSuccessActionInterface | GenreRequestActionInterface | GenreFailureActionInterface;