import React from "react";

import './reservation-date-selector.scss'

interface ReservationDateSelectorProps {
    className: string;
}

const ReservationDateSelectorComponent = (props: ReservationDateSelectorProps) => {
    const { className } = props;

    return (
        <div className={className}>
            ReservationDateSelector
        </div>
    )
}

export const ReservationDateSelector = ReservationDateSelectorComponent;