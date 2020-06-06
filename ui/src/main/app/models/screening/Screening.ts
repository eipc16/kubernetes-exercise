import {ObjectState} from "../infrastructure";
import {ScreeningRoomInterface} from "../screening-rooms/ScreeningRoom";

export interface ScreeningInterface {
    screeningId: number;
    movieId: number;
    screeningRoomId: number;
    screeningRoom: ScreeningRoomInterface;
    price: number;
    startTime: Date;
    endTime: Date;
    objectState: ObjectState;
}