import React from "react";

import './movie-details.scss';

interface MovieDetailsProps {
    id: number
}

export const MovieDetailsComponent = (props: MovieDetailsProps) => {
    const { id } = props

    return (
        <div>
            {id}
        </div>
    )
}