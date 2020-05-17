import {DateRange} from "./index";

export interface MovieListFiltersInterface {
    searchText?: string;
    genres?: string[];
    currentPage?: number;
    dateRange: DateRange;
}