import {Movie} from "../../../models/movies-list";
import {Card} from "antd";
import React from "react";

import './movie-list-entry.scss';
import Meta from "antd/es/card/Meta";

interface MovieListEntryProps {
    movie: Movie;
}

export const MovieListEntry = (props: MovieListEntryProps) => {
    const { movie } = props

    return (
        <Card className='movie--entry--card' hoverable>
            <img src={movie.posterUrl} alt={movie.title + '__poster'} />
            <Meta className='meta' title={movie.title} description={'Release Date: '+ movie.releaseDate} />
            <div className='thumbnail--overlay'>
                <h1>{movie.title}</h1>
                <a href="#" className="btn">
                    Reserve&nbsp;
                </a>
            </div>
        </Card>
    )
}