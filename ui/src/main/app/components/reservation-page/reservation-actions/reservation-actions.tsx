import React from "react";

import './reservation-actions.scss'
import {Button} from "antd";
import {connect} from "react-redux";
import {SeatActionPublisher, SeatsMap} from "../../../redux/actions/seat";
import {ReservationState} from "../../../models/screening-rooms";

interface OwnProps {
    className: string;
    seatActionPublisher: SeatActionPublisher;
}

interface ReservationProps {
    seats: SeatsMap
}

type ReservationActionsProps = OwnProps & ReservationProps

const ReservationActionsComponent = (props: ReservationActionsProps) => {
    let { className, seats } = props;
    let counter = Object.values(seats).filter(seat => seat.reservationState === ReservationState.SELECTED).length

    return (
        <div className={className} >
            <span>
                Your reservation:
            </span>
            <span>
                Number of seats: {counter}
            </span>
            <span>
                Total cost: {counter * 10} $
            </span>
            <Button>
                Reserve
            </Button>
        </div>
    )
}

const mapStateToProps = (store: any, ownProps: OwnProps) => {
    const seatsState = store.screeningSeats;
    return {
        seats: seatsState.seats,
        ...ownProps
    }
}

export const ReservationActions: React.FC<OwnProps> = connect(mapStateToProps)(ReservationActionsComponent);