import React, {useState} from 'react'

import './reservation-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";
import {MovieDetailsComponent} from "../movie-details/movie-details";
import {ReservationDateSelector} from "./reservation-date-selector/reservation-date-selector";
import {ReservationSeats} from "./reservation-seats-grid/reservation-seats-grid";
import {ReservationActions} from "./reservation-actions/reservation-actions";
import {SeatActionPublisherImpl} from "../../redux/actions/seat";
import {SeatServiceImpl} from "../../services/seat-service";

export const ReservationPage = (props: any) => {
    let history = useHistory();
    const [seatService] = useState(SeatServiceImpl.createInstance())
    const [seatActionPublisher] = useState(new SeatActionPublisherImpl(seatService))

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
                <MovieDetailsComponent movieId={props.match.params.id} className='reservation--movie--details'/>
                <ReservationDateSelector className='reservation--date--selector'/>
                <ReservationSeats screeningId={371} seatActionPublisher={seatActionPublisher} className='reservation--seats--grid'/>
                <ReservationActions className='reservation--actions'/>
            </div>
        </React.Fragment>
    )
}
