import {ObjectState} from "../infrastructure";

export interface ScreeningRoomInterface {
    id: number;
    number: number;
    rowsNumber: number;
    seatsInRowNumber: number;
    objectState: ObjectState;
}