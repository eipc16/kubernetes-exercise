import React, {ReactNode, useState} from "react";

import './reservation-actions.scss'
import {Button, Modal} from "antd";
import {connect, useDispatch} from "react-redux";
import {SeatActionPublisher, SeatsMap} from "../../../redux/actions/seat";
import {ReservationState} from "../../../models/screening-rooms";
import {ScreeningsState} from "../../../redux/reducers/screening-reducer";
import {useHistory} from "react-router-dom";
import {AlertContainer} from "../../alert/alert";
import {Alert} from "../../../models/infrastructure";
import {AlertTypes} from "../../../models/infrastructure/Alert";
import {Reservation} from "../../../models/reservation";
import {ReduxStore} from "../../../redux/reducers/root-reducer";

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

const ReservationActionsComponent = (props: ReservationActionsProps): JSX.Element => {
    const dispatch = useDispatch();
    const {className, seats, seatActionPublisher, screeningId, seatPrice} = props;
    const counter = Object.values(seats).filter(seat => seat.reservationState === ReservationState.SELECTED).length
    const [modalState, setModalState] = useState({
        loading: false, visible: false,
        content: {} as ReactNode
    });
    const history = useHistory();

    const showModal = (numOfSeats: number, totalPrice: string): void => {
        if (numOfSeats > 0) {
            setModalState({
                ...modalState, visible: true,
                content: <p>{`Number of seats: ${numOfSeats}\n Total cost: $${totalPrice}`}</p>
            });
        }
    };

    const alertSupplier = (message: string): Alert => {
        return {
            id: 'register-failure-alert',
            component: 'reservation--modal--alert',
            message: message,
            type: AlertTypes.ERROR,
            canDismiss: true
        }
    };

    const handleOk = (): void => {
        setModalState({...modalState, loading: true});
        const reservation: Reservation = {
            seatsIds: Object.values(seats).filter(seat => seat.reservationState === ReservationState.SELECTED).map(seat => seat.id),
            screeningId: screeningId
        };

        dispatch(seatActionPublisher.reserveSeats(reservation, alertSupplier,
            () => {
            setModalState({...modalState, loading: false })
            history.push('/')
        }, () => {
            setModalState({
                ...modalState,
                content: <p>Exception occurred during request.</p>,
                loading: false
            })
        }));
        setModalState({
                ...modalState,
                loading: true,
                content: <p>We are working on your reservation... You should be redirected to main page after success.</p>
            }
        );
    };

    const handleCancel = (): void => {
        setModalState({...modalState, visible: false});
    };

    const totalPrice = (counter * seatPrice).toFixed(2);

    return (
        <div className={className}>
            <Modal
                visible={modalState.visible}
                title="Submit reservation"
                onOk={(): void => showModal(counter, totalPrice)}
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
                <React.Fragment>
                    <AlertContainer component='reservation--modal--alert' />
                    <div className='modal--content'>
                        {modalState.content}
                    </div>
                </React.Fragment>
            </Modal>
            <div className='info--container'>
                <table className="tg">
                    <thead>
                    <tr>
                        <th className="column--name">Seats</th>
                        <th className="column--name">Price</th>
                        <th className="reserve--button" rowSpan={2}>
                            <Button onClick={(): void => showModal(counter, totalPrice)}
                                    disabled={counter < 1}>
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

const mapStateToProps = (store: ReduxStore, ownProps: OwnProps): ReservationActionsProps => {
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