import React, {useState} from 'react'

import './reservation-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";
import {MovieDetailsCom} from "../movie-details/movie-details";
import {ReservationDateSelector} from "./reservation-date-selector/reservation-date-selector";
import {ReservationSeats} from "./reservation-seats-grid/reservation-seats-grid";
import {ReservationActions} from "./reservation-actions/reservation-actions";
import {SeatActionPublisherImpl} from "../../redux/actions/seat";
import {SeatServiceImpl} from "../../services/seat-service";
import {ScreeningServiceImpl} from "../../services/screening-service";
import {ScreeningActionPublisherImpl} from "../../redux/actions/screening";
import {MovieDetailsActionPublisherImpl} from "../../redux/actions/movie-details";
import {MovieDetailsServiceImpl} from "../../services/movie-details-service";

export const ReservationPage = (props: any) => {
    let history = useHistory();
    const [movieDetailsService] = useState(MovieDetailsServiceImpl.createInstance)
    const [movieDetailsPublisher] = useState(new MovieDetailsActionPublisherImpl(movieDetailsService))
    const [seatService] = useState(SeatServiceImpl.createInstance());
    const [screeningService] = useState(ScreeningServiceImpl.createInstance());
    const [seatActionPublisher] = useState(new SeatActionPublisherImpl(seatService));
    const [screeningActionPublisher] = useState(new ScreeningActionPublisherImpl(screeningService));

    const movieId = props.match.params.id;

    const onClick = (e: React.MouseEvent<HTMLDivElement>) => {
        e.preventDefault()
        history.push('/')
    }

    return (
        <React.Fragment>
            <PageHeader
                className="site-page-header"
                onBack={onClick}
                title="Reservation page"
            />
            <div className='page--content'>
                <ReservationDateSelector movieId={movieId}
                                         screeningActionPublisher={screeningActionPublisher}
                                         className='reservation--date--selector'/>
                <ReservationSeats seatActionPublisher={seatActionPublisher}
                                  className='reservation--seats--grid'/>
                <ReservationActions className='reservation--actions'/>
                <MovieDetailsCom movieDetailsPublisher={movieDetailsPublisher} className='reservation--movie--details' movieId={props.match.params.id}/>
            </div>
        </React.Fragment>
    )
}
