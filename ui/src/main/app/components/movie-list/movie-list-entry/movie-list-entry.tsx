import {Movie} from "../../../models/movies-list";
import {Card} from "antd";
import React from "react";

import './movie-list-entry.scss';
import Meta from "antd/es/card/Meta";
import {useHistory} from "react-router-dom";

interface MovieListEntryProps {
    movie: Movie;
}

export const MovieListEntry = (props: MovieListEntryProps) => {
    const { movie } = props

    let history = useHistory();
    const onClick = () => {
        history.push('/reservation/' + movie.id)
    }

    return (
        <Card className='movie--entry--card' onClick={onClick} hoverable>
            <img src={movie.posterUrl} alt={movie.title + '__poster'} />
            <Meta className='meta' title={movie.title} description={'Release Date: '+ movie.releaseDate} />
            <div className='thumbnail--overlay'>
                <h1>{movie.title}</h1>
                <a href="/" className="btn">
                    Reserve&nbsp;
                </a>
            </div>
        </Card>
    )
}