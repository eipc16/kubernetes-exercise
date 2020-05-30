import React from "react";

import './reservation-actions.scss'

interface ReservationActionsProps {
    className: string;
}

const ReservationActionsComponent = (props: ReservationActionsProps) => {
    const { className } = props;

    return (
        <div className={className} >
            ReservationActionsComponent
        </div>
    )
}

export const ReservationActions = ReservationActionsComponent;