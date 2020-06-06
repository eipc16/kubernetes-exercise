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

interface ReservationPropsState {
    seats: SeatsMap;
    seatPrice: number;
    screeningId: number;
}

type ReservationActionsProps = OwnProps & ReservationPropsState

const ReservationActionsComponent = (props: ReservationActionsProps) => {
    const dispatch = useDispatch()
    let { className, seats, seatActionPublisher, screeningId, seatPrice } = props;
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
            text: 'Your reservation is done. You should be redirected to main page after a few seconds.'})
        setTimeout(() => {
            setModalState({ ...modalState, loading: false });
            history.push('/')
        }, 5000);
    };

    const handleCancel = () => {
        setModalState({ ...modalState, visible: false });
    };

    const totalPrice = (counter * seatPrice).toFixed(2);

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
            <div className='info--container'>
                <table className="tg">
                    <thead>
                    <tr>
                        <th className="column--name">Seats</th>
                        <th className="column--name">Price</th>
                        <th className="reserve--button" rowSpan={2}>
                            <Button onClick={showModal} disabled={counter < 1}>
                                Reserve
                            </Button>
                        </th>
                    </tr>
                    <tr>
                        <td className="column--value">{counter}</td>
                        <td className="column--value">${totalPrice}</td>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    )
};

const mapStateToProps = (store: any, ownProps: OwnProps) => {
    const seatsState = store.screeningSeats;
    const screeningsState: ScreeningsState = store.movieScreenings;
    const currentScreening = screeningsState.currentScreening;
    return {
        seats: seatsState.seats,
        seatPrice: currentScreening ? currentScreening.price : 0,
        screeningId: screeningsState.currentScreening?.screeningId || 0,
        ...ownProps
    }
};

export const ReservationActions: React.FC<OwnProps> = connect(mapStateToProps)(ReservationActionsComponent);