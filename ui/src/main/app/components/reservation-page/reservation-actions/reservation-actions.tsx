import React, {useState} from "react";

import './reservation-actions.scss'
import {Button, Modal} from "antd";
import {connect, useDispatch} from "react-redux";
import {SeatActionPublisher, SeatsMap} from "../../../redux/actions/seat";
import {ReservationState} from "../../../models/screening-rooms";
import {ScreeningsState} from "../../../redux/reducers/screening-reducer";
import {useHistory} from "react-router-dom";

interface OwnProps {
    className: string;
    seatActionPublisher: SeatActionPublisher;
}

interface ReservationProps {
    seats: SeatsMap;
    screeningId: number;
}

type ReservationActionsProps = OwnProps & ReservationProps

const ReservationActionsComponent = (props: ReservationActionsProps) => {
    const dispatch = useDispatch()
    let { className, seats, seatActionPublisher, screeningId } = props;
    let counter = Object.values(seats).filter(seat => seat.reservationState === ReservationState.SELECTED).length
    const [modalState, setModalState] = useState({loading: false, visible: false,
        text: ""})
    let history = useHistory();

    const showModal = () => {
        if (counter > 0) {
            setModalState({...modalState, visible: true,
                text: `Number of seats: ${counter}\n Total cost: $${counter * 10}`});
        }
    };

    const handleOk = () => {
        setModalState({ ...modalState, loading: true });
        dispatch(seatActionPublisher.reserveSeats({
            seatsIds: Object.values(seats).filter(seat => seat.reservationState === ReservationState.SELECTED).map(seat => seat.id),
            screeningId: screeningId}))
        setModalState( {...modalState, loading: true,
            text: 'Your reservation is done. You will be redirect to main page after few seconds.'})
        setTimeout(() => {
            setModalState({ ...modalState, loading: false });
            history.push('/')
        }, 5000);
    };

    const handleCancel = () => {
        setModalState({ ...modalState, visible: false });
    };

    return (
        <div className={className} >
            <Modal
                visible={modalState.visible}
                title="Submit reservation"
                onOk={showModal}
                onCancel={handleCancel}
                footer={[
                    <Button key="back" onClick={handleCancel}>
                        Cancel
                    </Button>,
                    <Button key="submit" type="primary" loading={modalState.loading} onClick={handleOk}>
                        Submit
                    </Button>,
                ]}
            >
                {modalState.text}
            </Modal>
            <span>
                Your reservation:
            </span>
            <span>
                Number of seats: {counter}
            </span>
            <span>
                Total cost: {counter * 10} $
            </span>
            <Button onClick={showModal} disabled={counter < 1}>
                Reserve
            </Button>
        </div>
    )
}

const mapStateToProps = (store: any, ownProps: OwnProps) => {
    const seatsState = store.screeningSeats;
    const screeningsState: ScreeningsState = store.movieScreenings;
    return {
        seats: seatsState.seats,
        screeningId: screeningsState.currentScreening?.screeningId || 0,
        ...ownProps
    }
}

export const ReservationActions: React.FC<OwnProps> = connect(mapStateToProps)(ReservationActionsComponent);