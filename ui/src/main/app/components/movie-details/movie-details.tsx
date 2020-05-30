import React from "react";

import './movie-details.scss';

interface MovieDetailsProps {
    movieId: number;
    className?: string;
}

export const MovieDetailsComponent = (props: MovieDetailsProps) => {
    const { movieId, className } = props

    return (
        <div className={className}>
            Details for: {movieId}
        </div>
    )
}