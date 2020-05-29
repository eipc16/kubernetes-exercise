import React from 'react'

import './reservation-page.scss'
import {PageHeader} from "antd";
import {useHistory} from "react-router-dom";

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
            <div>
                {props.match.params.id}
            </div>
        </div>
    )
}
