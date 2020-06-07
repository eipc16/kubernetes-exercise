import {Alert} from "../../../models/infrastructure";
import {Action, Dispatch} from "redux";
import {AlertPublisher, AlertPublisherImpl} from "../alert";
import {GenreService} from "../../../services/genre-service";
import {genreConstants} from "../../constants";
import {GenreFailureActionInterface, GenreRequestActionInterface, GenreSuccessActionInterface} from "./types";
import {Genre} from "../../../models/genre";

export interface GenreActionPublisher {
    fetchGenres(searchText?: string, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void;
}

export class GenreActionPublisherImpl implements GenreActionPublisher {
    genreService: GenreService;
    alertPublisher: AlertPublisher;

    constructor(genreService: GenreService, alertPublisher?: AlertPublisher) {
        this.genreService = genreService;
        this.alertPublisher = alertPublisher || AlertPublisherImpl.createInstance()
    }

    fetchGenres(searchText?: string, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void {
        function request(): GenreRequestActionInterface {
            return {
                type: genreConstants.GENRES_REQUEST
            }
        }

        function success(genres: Genre[]): GenreSuccessActionInterface {
            return {
                type: genreConstants.GENRES_SUCCESS,
                genreList: genres
            }
        }

        function failure(error: string): GenreFailureActionInterface {
            return {
                type: genreConstants.GENRES_FAILURE,
                error: error
            }
        }

        return (dispatch: Dispatch<Action>): void => {
            dispatch(request());

            this.genreService.fetchGenres(searchText)
                .then(
                    (genres: Genre[]) => {
                        dispatch(success(genres))
                    },
                    (errorResponse: string) => {
                        dispatch(failure(errorResponse))
                        if (errorAlertSupplier) {
                            const alert = errorAlertSupplier(errorResponse)
                            this.alertPublisher.pushAlert(alert)(dispatch)
                        }
                    }
                )

        };
    }

}