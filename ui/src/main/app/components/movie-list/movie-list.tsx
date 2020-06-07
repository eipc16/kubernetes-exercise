import React from 'react'
import {Col, Row} from 'antd'
import {connect} from "react-redux";
import {MovieListActionPublisher} from "../../redux/actions/movie-list";
import {MovieListEntry} from "./movie-list-entry/movie-list-entry";
import {MovieListFilters, PlayerMovies} from '../../models/movies-list';

import './movie-list.scss';
import {MovieListFiltersContainer} from "./movie-list-filters/movie-list-filters-container";
import {useFetching} from "../../utils/custom-fetch-hook";
import {PlayedMoviesInterface} from "../../models/movies-list/PlayedMovies";
import {ReduxStore} from "../../redux/reducers/root-reducer";

interface OwnProps {
    movieListPublisher: MovieListActionPublisher;
}

interface State {
    isAuthenticated: boolean;
    isFetched?: boolean;
    isFetching?: boolean;
    movieList: PlayerMovies;
    filters: MovieListFilters;
}

type MovieListProps = State & OwnProps

const MovieListComponent = (props: MovieListProps): JSX.Element => {
    const {isFetching, movieList, movieListPublisher, filters, isAuthenticated} = props;

    useFetching(movieListPublisher.getMovieListByFilters(filters), [filters]);

    const areMoviesAvailable = (movieList: PlayedMoviesInterface): boolean => {
        return movieList && movieList.content && movieList.content.length > 0;
    };

    return (
        <div className='main--list--container'>
            <MovieListFiltersContainer movieListActionPublisher={movieListPublisher}>
                { isFetching ? (
                    <div className='info--message fetching--message'>Fetching..</div>
                ) : (
                    <Row className='list--row' gutter={[16, 16]}>
                        {areMoviesAvailable(movieList) ? (
                            areMoviesAvailable(movieList) && movieList.content.map(movie =>
                                <Col key={movie.imdbId}>
                                    <MovieListEntry movie={movie} isAuthenticated={isAuthenticated}/>
                                </Col>
                            )
                        ) : (
                            <div className='info--message'>
                                <span className='message--content'>Could not find any movies</span>
                                <span className='span--icon'>&#9785;</span>
                            </div>
                        )}
                    </Row>
                )}
            </MovieListFiltersContainer>
        </div>
    )
};

const defaultMovieList = {
    content: [],
    pageable: {
        pageSize: 0,
        pageNumber: 0
    },
    last: true,
    totalElements: 0,
    totalPages: 0,
    first: true,
    number: 0
};

const mapStateToProps = (state: ReduxStore, ownProps: OwnProps): MovieListProps => ({
    isAuthenticated: state.auth.loggedIn || false,
    isFetched: state.movieList.isFetched,
    isFetching: state.movieList.isFetching,
    movieList: state.movieList.playedMovies || defaultMovieList,
    filters: state.movieList.filters,
    ...ownProps
});

export const MovieList: React.FC<OwnProps> = connect(mapStateToProps)(MovieListComponent);
