import {Movie} from "../../../models/movies-list";
import {Card} from "antd";
import React from "react";

import './movie-list-entry.scss';
import Meta from "antd/es/card/Meta";
import {Link} from "react-router-dom";

interface MovieListEntryProps {
    movie: Movie;
}

export const MovieListEntry = (props: MovieListEntryProps) => {
    const {movie} = props;

    const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>) => {
        e.preventDefault();
        e.currentTarget.src = "not_found.jpg"
    }

    return (
        <Card className='movie--entry--card' hoverable>
            <img className='movie--poster' src={movie.posterUrl} alt={movie.title + '__poster'} onError={handleImageError} />
            <Meta className='meta' title={movie.title} description={'Release Date: ' + movie.releaseDate}/>
            <div className='thumbnail--overlay'>
                <h1>{movie.title}</h1>
                <Link className='btn' to={`/reservation/${movie.id}`}>
                    Reserve&nbsp;
                </Link>
            </div>
        </Card>
    )
}