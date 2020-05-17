import React from 'react'
import {Col, Row} from 'antd'
import {connect} from "react-redux";
import {MovieListActionPublisher} from "../../redux/actions/movie-list";
import {MovieListEntry} from "./movie-list-entry/movie-list-entry";
import {MovieListFilters, PlayerMovies} from '../../models/movies-list';

import './movie-list.scss';
import {MovieListFiltersContainer} from "./movie-list-filters/movie-list-filters-container";
import {useFetching} from "../../utils/custom-fetch-hook";

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

    return (
        <div className='main--list--container'>
            <MovieListFiltersContainer movieListActionPublisher={movieListPublisher} totalPages={movieList ? movieList.totalPages : 1}>
                { isFetching ? (
                    <div className='fetching--message'>Fetching..</div>
                ) : (
                    <Row className='list--row' gutter={[16, 16]}>
                        {movieList && movieList.content && movieList.content.map(movie =>
                            <Col key={movie.imdbId}>
                                <MovieListEntry movie={movie}/>
                            </Col>
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
