import { Movie } from './index'
import {Pageable} from "../infrastructure";

export interface PlayedMoviesInterface {
    content: Movie[];
    pageable: Pageable;
    last: boolean;
    totalElements: number;
    totalPages: number;
    first: boolean;
    number: number;
}