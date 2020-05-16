import React, {useEffect, useState} from 'react';

import {MovieListComponent} from "../movie-list/movie-list";
import {MovieListActionPublisherImpl} from "../../redux/actions/movie-list";
import {connect, useDispatch} from "react-redux";
import {MovieList} from "../../models/movies-list";
import {Button, PageHeader} from "antd";
import './main-page.scss';
import {MovieListServiceImpl} from "../../services";
import {Action, Dispatch} from "redux";

interface OwnProps {
}

interface State {
    isFetched?: boolean;
    isFetching?: boolean;
    movieList: MovieList
}

type MainPageProps = State & OwnProps;

const MainPageComponent : React.FC<MainPageProps> = (props: MainPageProps) => {
    const {movieList} = props;
    const [movieListService, ] = useState(MovieListServiceImpl.createInstance())
    const [movieListPublisher, ] = useState(new MovieListActionPublisherImpl(movieListService))
    const dispatch = useDispatch();

    useEffect(() => {
        const searchEvent = movieListPublisher.getMovieList({
            beginDate: Date.now(),
            endDate: Date.now()
        });
        dispatch(searchEvent);
    }, [])

    return (
        <div>
            <PageHeader className='site-page-header'
                        title='Cinema tickets sale system'
                        extra={[<Button key="1" type="primary">
                            Log in
                        </Button>]}>
            </PageHeader>
            { movieList && <MovieListComponent list={movieList.list}/> }
        </div>
    )
};

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
    isFetched: state.movieList.isFetched,
    isFetching: state.movieList.isFetching,
    movieList: state.movieList.movieList,
    ...ownProps
});

export const MainPage: React.FC<OwnProps> = connect(mapStateToProps)(MainPageComponent);