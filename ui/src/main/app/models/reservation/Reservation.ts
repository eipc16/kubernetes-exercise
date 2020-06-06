import {ObjectState} from "../infrastructure";

export interface ReservationInterface {
    id?: number;
    screeningId: number;
    seatsIds: number[];
    reservedByUser?: number;
    objectState?: ObjectState;
}