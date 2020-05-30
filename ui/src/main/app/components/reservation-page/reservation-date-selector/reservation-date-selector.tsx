import React from "react";

import './reservation-date-selector.scss'
import {ScreeningActionPublisher} from "../../../redux/actions/screening";
import {useFetching} from "../../../utils/custom-fetch-hook";
import {Screening} from "../../../models/screening";
import {ScreeningsState} from "../../../redux/reducers/screening-reducer";
import {connect} from "react-redux";

interface OwnProps {
    className: string;
    movieId: number;
    screeningActionPublisher: ScreeningActionPublisher;
}

interface ReservationDateSelectorState {
    screenings: Screening[];
    currentScreening?: Screening;
    isFetching: boolean;
}

export type ReservationDateSelectorProps = OwnProps & ReservationDateSelectorState;

const ReservationDateSelectorComponent = (props: ReservationDateSelectorProps) => {
    const { className, movieId, screeningActionPublisher, isFetching } = props;

    useFetching(screeningActionPublisher.fetchScreenings(movieId), [movieId]);

    if(isFetching) {
        return (
            <div className='info--message'>
                Fetching screenings...
            </div>
        )
    }

    return (
        <div className={className}>
            Screening selector
            {/*{*/}
            {/*    screenings ? (*/}
            {/*        screenings.map(screening => (*/}
            {/*            <p key={screening.screeningId}>Time: {screening.startTime}</p>*/}
            {/*        ))*/}
            {/*    ) : (*/}
            {/*        <div className='info--message'>*/}
            {/*            No screenings found...*/}
            {/*        </div>*/}
            {/*    )*/}
            {/*}*/}
        </div>
    )
}

const mapStateToProps = (store: any, ownProps: OwnProps) => {
    const screeningState: ScreeningsState = store.movieScreenings;
    return {
        screenings: screeningState.screenings,
        currentScreening: screeningState.currentScreening,
        isFetching: screeningState.isFetching,
        ...ownProps
    }
}


export const ReservationDateSelector: React.FC<OwnProps> = connect(mapStateToProps)(ReservationDateSelectorComponent);