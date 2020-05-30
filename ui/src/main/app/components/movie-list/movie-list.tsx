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

interface OwnProps {
    movieListPublisher: MovieListActionPublisher
}

interface State {
    isFetched?: boolean;
    isFetching?: boolean;
    movieList: PlayerMovies;
    filters: MovieListFilters;
}

type MovieListProps = State & OwnProps

const MovieListComponent = (props: MovieListProps) => {
    const {isFetching, movieList, movieListPublisher, filters} = props

    useFetching(movieListPublisher.getMovieListByFilters(filters), [filters])

    // if (isFetching) {
    //     return <div>Fetching..</div>
    // }

    const areMoviesAvailable = (movieList: PlayedMoviesInterface) => {
        return movieList && movieList.content && movieList.content.length > 0
    }

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
                                    <MovieListEntry movie={movie}/>
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
}

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
    isFetched: state.movieList.isFetched,
    isFetching: state.movieList.isFetching,
    movieList: state.movieList.playedMovies,
    filters: state.movieList.filters,
    ...ownProps
})

export const MovieList: React.FC<OwnProps> = connect(mapStateToProps)(MovieListComponent)
