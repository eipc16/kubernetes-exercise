import {Movie} from "../../../models/movies-list";
import {Card} from "antd";
import React from "react";

import './movie-list-entry.scss';
import Meta from "antd/es/card/Meta";
import {Link} from "react-router-dom";

interface MovieListEntryProps {
    isAuthenticated: boolean;
    movie: Movie;
}

export const MovieListEntry = (props: MovieListEntryProps): JSX.Element => {
    const { movie, isAuthenticated } = props;

    const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>): void => {
        e.preventDefault();
        e.currentTarget.src = "not_found.jpg"
    };

    const getReservationLink = (movie: Movie): string => {
        let path = `/screenings/${movie.id}`;
        if(!isAuthenticated) {
            path = `/login?redirectPath=${path}`
        }
        return path
    };

    return (
        <Card className='movie--entry--card' hoverable>
            <img className='movie--poster' src={movie.posterUrl} alt={movie.title + '__poster'}
                 onError={handleImageError} />
            <Meta className='meta' title={movie.title} description={'Release Date: ' + movie.releaseDate}/>
            <div className='thumbnail--overlay'>
                <h1>{movie.title}</h1>
                <Link className='btn' to={getReservationLink(movie)}>
                    Reserve&nbsp;
                </Link>
            </div>
        </Card>
    )
};