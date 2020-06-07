import {screeningReducer} from "../screening-reducer";
import {screeningConstants} from "../../constants/screening-constants";
import {ObjectState} from "../../../models/infrastructure";

describe('Screening reducer', () => {
    it('should handle SCREENING_REQUEST', () => {
        expect(
            screeningReducer({screenings: [], isFetching: false, isFetched: false}, {
                type: screeningConstants.MOVIE_SCREENINGS_REQUEST, movieId: 0
            })
        ).toEqual(
            {
                isFetched: false,
                isFetching: true,
                screenings: []
            }
        )
    })
    it('should handle SCREENING_FAILURE', () => {
        expect(
            screeningReducer({screenings: [], isFetching: true, isFetched: false}, {
                type: screeningConstants.MOVIE_SCREENINGS_FAILURE, error: "Error"
            })
        ).toEqual(
            {
                isFetching: false,
                isFetched: false,
                screenings: []
            }
        )
    })
    it('should handle SCREENING_SUCCESS empty list', () => {
        expect(
            screeningReducer({screenings: [], isFetching: true, isFetched: false}, {
                type: screeningConstants.MOVIE_SCREENINGS_SUCCESS, screenings: []
            })
        ).toEqual(
            {
                isFetched: true,
                isFetching: false,
                screenings: [],
                currentScreening: undefined
            }
        )
    })
    it('should handle SCREENING_SUCCESS not empty list', () => {
        const list = [{movieId: 0, screeningId: 0, price: 5, screeningRoomId: 0, startTime: Date.prototype,
            screeningRoom:{id: 0, number: 0, rowsNumber: 0, seatsInRowNumber: 0, objectState: ObjectState.ACTIVE},
            endTime: Date.prototype, objectState: ObjectState.ACTIVE}]
        expect(
            screeningReducer({screenings: [], isFetching: true, isFetched: false}, {
                type: screeningConstants.MOVIE_SCREENINGS_SUCCESS, screenings: list
            })
        ).toEqual(
            {
                isFetched: true,
                isFetching: false,
                screenings: list,
                currentScreening: list[0]
            }
        )
    })
    it('should handle SET_CURRENT_SCREENING', () => {
        const list = [{movieId: 0, screeningId: 0, price: 5, screeningRoomId: 0, startTime: Date.prototype,
            screeningRoom:{id: 0, number: 0, rowsNumber: 0, seatsInRowNumber: 0, objectState: ObjectState.ACTIVE},
            endTime: Date.prototype, objectState: ObjectState.ACTIVE}]
        expect(
            screeningReducer({screenings: list, isFetching: false, isFetched: true}, {
                type: screeningConstants.SET_CURRENT_SCREENING, screeningId: 0
            })
        ).toEqual(
            {
                isFetched: true,
                isFetching: false,
                screenings: list,
                currentScreening: list[0]
            }
        )
    })
})