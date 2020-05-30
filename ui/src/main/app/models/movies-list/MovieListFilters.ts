import {DateRange} from "./index";
import {Pageable} from "../infrastructure";

export interface MovieListFiltersInterface {
    searchText?: string;
    genres?: string[];
    pageOptions: Pageable;
    dateRange: DateRange;
}