import React from "react";

import './reservation-actions.scss'
import {Button} from "antd";

interface ReservationActionsProps {
    className: string;
}

const ReservationActionsComponent = (props: ReservationActionsProps) => {
    let { className } = props;



    return (
        <div className={className} >
            <span>
                Your reservation:
            </span>
            <span>
                Number of seats: 0
            </span>
            <Button>
                Reserve
            </Button>
        </div>
    )
}

export const ReservationActions = ReservationActionsComponent;