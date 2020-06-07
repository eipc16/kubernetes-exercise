import React, {useState} from 'react';
import Search from "antd/es/input/Search";
import {DatePicker, Pagination, Select} from "antd";
import {connect, useDispatch} from "react-redux";
import {GenreActionPublisherImpl} from "../../../redux/actions/genre";
import {Genre} from "../../../models/genre";
import {GenreServiceImpl} from "../../../services/genre-service";
import {useFetching} from "../../../utils/custom-fetch-hook";
import {DateRange, MovieListFilters} from "../../../models/movies-list";
import {MovieListActionPublisher} from "../../../redux/actions/movie-list";
import moment from 'moment';

import './movie-list-filters.scss';
import {ReduxStore} from "../../../redux/reducers/root-reducer";

const {Option} = Select;
const {RangePicker} = DatePicker;

interface OwnProps {
    movieListActionPublisher: MovieListActionPublisher;
    children?: React.ReactNode;
}

interface State {
    genreFetched: boolean;
    genreFetching: boolean;
    genres: Genre[];
    totalGenres: number;
    filters: MovieListFilters;
    totalMovies: number;
}

enum FiltersEnum {
    SEARCH_TEXT = "searchText",
    DATARANGE = "dateRange",
    GENRES = "genres",
    PAGE = "pageOptions"
}

type FilterValue = DateRange & string[] & string & number & undefined;

export type MovieListFiltersProps = OwnProps & State;

export const MovieListFiltersComponent = (props: MovieListFiltersProps): JSX.Element => {
    const {genres, totalGenres, genreFetching, movieListActionPublisher, filters, totalMovies, children} = props;
    const dispatch = useDispatch();

    // State holders
    let searchText = filters.searchText;
    let dateRange = filters.dateRange;
    let selectedGenres = filters.genres;
    const pageOptions = filters.pageOptions;

    const [genreService,] = useState(GenreServiceImpl.createInstance());
    const [genrePublisher,] = useState(new GenreActionPublisherImpl(genreService));

    const updateFilters = (lastUpdated: FiltersEnum, value: FilterValue): void => {
        dispatch(movieListActionPublisher.updateFilters({
            dateRange: dateRange,
            searchText: searchText,
            genres: selectedGenres,
            pageOptions: { ...pageOptions, pageNumber: 0},
            [lastUpdated.toString()]: value
        }))
    };

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const handleDatePicker = (value: any): void => {
        const newRange: DateRange = {
            beginDate: value[0].valueOf(),
            endDate: value[1].valueOf()
        };
        dateRange = newRange;
        updateFilters(FiltersEnum.DATARANGE, newRange);
    };

    const handleGenreSelect = (value: string[]): void => {
        selectedGenres = value;
        updateFilters(FiltersEnum.GENRES, value);
    };

    const handleSearchText = (e: React.ChangeEvent): void => {
        const value = (e.target as HTMLInputElement).value;
        const finalSearchText = value ? value : "";
        searchText = finalSearchText;
        updateFilters(FiltersEnum.SEARCH_TEXT, finalSearchText);
    };

    const handlePage = (page: number, moviesPerPage?: number): void => {
        updateFilters(FiltersEnum.PAGE, {
            pageNumber: page - 1,
            pageSize: moviesPerPage
        });
    };

    const getPlaceHolder = (allGenres: number): string => {
        if (allGenres < 1) {
            return 'No genres';
        }
        return `Select genre. Total genres: ${allGenres}`
    };

    useFetching(genrePublisher.fetchGenres());

    return (
        <div className='movie--list-filters'>
            <div className='top--filters--controls'>
                <div className='input--container title--input'>
                    <label className='input--label' htmlFor='title--input'>Title:</label>
                    <Search
                        id='title--input'
                        placeholder="Movie title"
                        enterButton="Search"
                        className='search--text--input'
                        size="large"
                        onChange={handleSearchText}
                    />
                </div>
                <div className='input--container'>
                    <label className='input--label' htmlFor='date--picker'>Date:</label>
                    <RangePicker
                        id='date--picker'
                        className='date--picker--input'
                        defaultValue={[
                            moment(dateRange.beginDate).utc(),
                            moment(dateRange.endDate).utc()
                        ]}
                        onChange={handleDatePicker}
                        showTime
                    />
                </div>
                <div className='input--container genre--container'>
                    <label className='input--label' htmlFor='genre--picker'>Genre:</label>
                    <Select
                        id='genre--picker'
                        mode="multiple"
                        className='genre--picker--input'
                        placeholder={getPlaceHolder(totalGenres)}
                        onChange={handleGenreSelect}
                    >
                        {
                            genreFetching ? "Fetching genres..." :
                                genres.map(genre => <Option key={genre.name} value={genre.name}>{genre.name}</Option>)
                        }
                    </Select>
                </div>
            </div>
            {children}
            <Pagination className='pagination--selector'
                        defaultCurrent={1}
                        pageSize={pageOptions.pageSize}
                        total={totalMovies}
                        onChange={handlePage}
                        onShowSizeChange={handlePage}
                        showSizeChanger={true}
                        responsive={true}
                        hideOnSinglePage={true}
            />
        </div>
    )
}

const mapStateToProps = (state: ReduxStore, ownProps: OwnProps): MovieListFiltersProps => ({
    genreFetched: state.genres.isFetched,
    genreFetching: state.genres.isFetching,
    genres: state.genres.genreList,
    totalGenres: state.genres.totalGenres,
    filters: state.movieList.filters,
    totalMovies: state.movieList.playedMovies ? state.movieList.playedMovies.totalElements : 1,
    ...ownProps
});

export const MovieListFiltersContainer: React.FC<OwnProps> = connect(mapStateToProps)(MovieListFiltersComponent);
