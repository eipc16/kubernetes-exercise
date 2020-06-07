import React, {useState} from "react";

import './movie-details.scss';
import {useFetching} from "../../utils/custom-fetch-hook";
import {MovieDetailsActionPublisher} from "../../redux/actions/movie-details";
import {MovieDetails} from "../../models/movie-details";
import {connect} from "react-redux";
import {Button, Drawer} from "antd";
import {MovieDetailsInterface} from "../../models/movie-details/movie-details";
import {ReduxStore} from "../../redux/reducers/root-reducer";

interface OwnProps {
    movieDetailsPublisher: MovieDetailsActionPublisher;
    movieId: number;
    className?: string;
}

interface State {
    isFetched?: boolean;
    isFetching?: boolean;
    movie?: MovieDetails;
}

type MovieDetailsProps = State & OwnProps

const MovieDetailsComponent = (props: MovieDetailsProps): JSX.Element => {
    const {movie, className, movieId, movieDetailsPublisher} = props;

    const [visible, setVisible] = useState(false);
    const showDrawer = (): void => {
        setVisible(true);
    };
    const onClose = (): void => {
        setVisible(false);
    };

    useFetching(movieDetailsPublisher.getMovieDetails(movieId), [movieId]);

    const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>): void => {
        e.preventDefault();
        e.currentTarget.src = "not_found.jpg"
    };

    const MovieDetailsInformation = (props: { movie?: MovieDetailsInterface; className: string }): JSX.Element => {
        const {movie, className} = props;

        if (!movie) {
            return (
                <div className={'info--message'}>
                    Movie not found.
                </div>
            )
        }

        return (
            <div className={className}>
                <img className='poster--image' src={movie.posterUrl} alt={movie.title + '__poster'}
                     onError={handleImageError}/>
                <h3>{movie.title}</h3>
                <p>
                    Director: {movie.director} <br/>
                    Release date: {movie.releaseDate} <br/>
                    Actors: {movie.actors} <br/>
                    Runtime: {movie.runtime} <br/>
                    Plot description: {movie.plot}
                </p>
            </div>
        )
    };

    return (
        <div className={className}>
            <MovieDetailsInformation movie={movie} className='movie--details'/>
            <Button className={'button--show--more'} type="primary" onClick={showDrawer}>
                Read more about movie
            </Button>
            <Drawer
                title="About movie"
                placement="left"
                width="100%"
                closable={true}
                onClose={onClose}
                visible={visible}
            >
                <MovieDetailsInformation movie={movie} className='movie--details2'/>
            </Drawer>
        </div>
    )
};

const mapStateToProps = (state: ReduxStore, ownProps: OwnProps): MovieDetailsProps => ({
    isFetched: state.movieDetails.isFetched,
    isFetching: state.movieDetails.isFetching,
    movie: (state.movieDetails.movie && state.movieDetails.movie.list.length > 0) ? state.movieDetails.movie.list[0] : undefined,
    ...ownProps
});

export const MovieDetailsCom: React.FC<OwnProps> = connect(mapStateToProps)(MovieDetailsComponent);
