import React, {useState} from "react";

import './movie-details.scss';
import {useFetching} from "../../utils/custom-fetch-hook";
import {MovieDetailsActionPublisher} from "../../redux/actions/movie-details";
import {MovieDetails} from "../../models/movie-details";
import {connect} from "react-redux";
import {Button, Drawer} from "antd";

interface OwnProps {
    movieDetailsPublisher: MovieDetailsActionPublisher,
    movieId: number,
    className?: string;
}

interface State {
    isFetched?: boolean;
    isFetching?: boolean;
    movie: MovieDetails[];
}

type MovieDetailsProps = State & OwnProps

const MovieDetailsComponent = (props: MovieDetailsProps) => {
    const { movie, className, movieId, movieDetailsPublisher } = props

    const [visible, setVisible] = useState(false);
    const showDrawer = () => {
        setVisible(true);
    };
    const onClose = () => {
        setVisible(false);
    };

    useFetching(movieDetailsPublisher.getMovieDetails(movieId), [movieId])

    const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>) => {
        e.preventDefault();
        e.currentTarget.src = "not_found.jpg"
    }

    if(!movie) {
        return (
            <div className={'info--message'}>
                Movie not found.
            </div>
        )
    }

    return (
        <div className={className}>
            <div className={'movie--details'}>
                <img src={movie[0].posterUrl} alt={movie[0].title + '__poster'} onError={handleImageError}/>
                <h3>{movie[0].title}</h3>
                <p>
                    Director: {movie[0].director} <br/>
                    Release date: {movie[0].releaseDate} <br/>
                    Actors: {movie[0].actors} <br/>
                    Runtime: {movie[0].runtime} <br/>
                    Plot description: {movie[0].plot}
                </p>
            </div>
            <Button className={'button--show--more'} type="primary" onClick={showDrawer}>
                Read more about movie
            </Button>
            <Drawer
                title="About movie"
                placement="left"
                closable={false}
                onClose={onClose}
                visible={visible}
            >
                <div className={'movie--details2'}>
                    <img src={movie[0].posterUrl} alt={movie[0].title + '__poster'} onError={handleImageError}/>
                    <h3>{movie[0].title}</h3>
                    <p>
                        Director: {movie[0].director} <br/>
                        Release date: {movie[0].releaseDate} <br/>
                        Actors: {movie[0].actors} <br/>
                        Runtime: {movie[0].runtime} <br/>
                        Plot description: {movie[0].plot}
                    </p>
                </div>
            </Drawer>
        </div>
    )
}

const mapStateToProps = (state: any, ownProps: OwnProps) => ({
    isFetched: state.movieDetails.isFetched,
    isFetching: state.movieDetails.isFetching,
    movie: state.movieDetails.movie,
    ...ownProps
})

export const MovieDetailsCom: React.FC<OwnProps> = connect(mapStateToProps)(MovieDetailsComponent)
