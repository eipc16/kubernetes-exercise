import React, {useState} from "react";

import './reservation-actions.scss'
import {Button, Modal} from "antd";
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


    const [modalState, setModalState] = useState({loading: false, visible: false})

    const showModal = () => {
        if (counter > 0) {
            setModalState({...modalState, visible: true});
        }
    };

    const handleOk = () => {
        setModalState({ ...modalState, loading: true });
        setTimeout(() => {
            setModalState({ loading: false, visible: false });
        }, 3000);
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
                        Return
                    </Button>,
                    <Button key="submit" type="primary" loading={modalState.loading} onClick={handleOk}>
                        Submit
                    </Button>,
                ]}
            >
                <p>
                    Number of seats: {counter}
                </p>
                <p>
                    Total cost: {counter * 10} $
                </p>
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
    return {
        seats: seatsState.seats,
        ...ownProps
    }
}

export const ReservationActions: React.FC<OwnProps> = connect(mapStateToProps)(ReservationActionsComponent);