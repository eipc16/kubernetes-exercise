import React from 'react'

import './reservation-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";
import {MovieDetailsComponent} from "../movie-details/movie-details";

export const ReservationPage = (props: any) => {
    let history = useHistory();
    const onClick = () => {
        history.push('/')
    }

    return (
        <div>
            <PageHeader
                className="site-page-header"
                onBack={() => onClick()}
                title="Reservation page"
            />
            <MovieDetailsComponent id={props.match.params.id}/>
        </div>
    )
}
