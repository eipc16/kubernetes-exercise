import React from "react";

import './reservation-seats-grid.scss'
import {ReservationState, Seat} from "../../../models/screening-rooms";
import {TransformComponent, TransformWrapper} from "react-zoom-pan-pinch/dist";
import {SeatActionPublisher, SeatsMap} from "../../../redux/actions/seat";
import {connect, useDispatch} from "react-redux";
import {useFetching} from "../../../utils/custom-fetch-hook";

interface OwnProps {
    className?: string;
    screeningId: number;
    seatActionPublisher: SeatActionPublisher;
}

interface SeatGridState {
    seats: SeatsMap;
    isFetching: boolean;
}

interface SeatLayout {
    [row: number]: Seat[];
}

interface SeatGridProps {
    columns: number[];
    rows: number[];
    seats: Seat[];
    onSeatClick: (seat: Seat) => void;
}

export type ReservationSeatsGridProps = SeatGridState & OwnProps;

const SeatsGridComponent = (props: SeatGridProps) => {
    const { columns, rows, seats, onSeatClick } = props;

    const mapSeatListToRows = (seatsToMap: Seat[]) => {
        let resultMap: SeatLayout = {}
        for(let i = 0; i < seatsToMap.length; i++) {
            let seat = seatsToMap[i];
            if(resultMap[seat.rowNumber]) {
                resultMap[seat.rowNumber].push(seat)
            } else {
                resultMap[seat.rowNumber] = [seat]
            }
        }
        return resultMap;
    }

    const getSeatColor = (seat: Seat) => {
        switch(seat.reservationState) {
            case ReservationState.AVAILABLE:
                return 'gray';
            case ReservationState.RESERVED:
                return 'red';
            case ReservationState.RESERVED_BY_YOU:
                return 'green';
            case ReservationState.SELECTED:
                return "orange";
            default:
                return 'gray';
        }
    }

    const getSeatStyle = (seat: Seat) => {
        return {
            backgroundColor: getSeatColor(seat)
        }
    }

    const handleSeatClick = (e: React.MouseEvent<HTMLTableDataCellElement>, seat: Seat) => {
        e.preventDefault();
        onSeatClick(seat);
    }

    if(!seats || seats.length < 1) {
        return (
            <div className='message--info'>
                Sorry! Could not find any seats!
            </div>
        )
    }

    const seatsMap = mapSeatListToRows(seats);
    console.log(seatsMap);
    return (
        <div className='seat--grid'>
            <p className='screen'>Screen</p>
            <table className='seats'>
                <thead>
                <tr>
                    <td key='empty' className='single--seat empty'> </td>
                    <React.Fragment>
                        {
                            columns.map(column => (<td key={column} className='single--seat no--seat'>{column}</td>))
                        }
                    </React.Fragment>
                </tr>
                </thead>
                <tbody>
                {
                    rows.map(row =>
                        <tr key={row}>
                            <td key={row} className='single--seat no--seat'>{row}</td>
                            <React.Fragment>
                                {
                                    seatsMap[row].map(seat => (
                                        <td key={seat.id}
                                            style={getSeatStyle(seat)}
                                            className='single--seat'
                                            onClick={(e) => handleSeatClick(e, seat)}>
                                                {seat.seatNumber}
                                        </td>
                                    ))
                                }
                            </React.Fragment>
                        </tr>
                    )
                }
                </tbody>
            </table>
        </div>
    )
}

const ReservationSeatsComponent = (props: ReservationSeatsGridProps) => {
    const { seats, isFetching, seatActionPublisher, screeningId } = props;
    const dispatch = useDispatch();

    useFetching(seatActionPublisher.fetchSeats(screeningId), [screeningId])

    const createArrayWithGivenSize = (size: number) => {
        let rowsList: number[] = [];
        for(let i = 0; i < size; i++) {
            rowsList.push(i + 1)
        }
        return rowsList;
    }

    const onSeatClick = (seat: Seat) => {
        console.log(seat);
        switch(seat.reservationState) {
            case ReservationState.AVAILABLE:
                return dispatch(seatActionPublisher.updateSeatReservationState(seat.id, ReservationState.SELECTED));
            case ReservationState.SELECTED:
                return dispatch(seatActionPublisher.updateSeatReservationState(seat.id, ReservationState.AVAILABLE));
            default:
                return;
        }
    }

    if(isFetching) {
        return (
            <div className='message--info'>
                Fetching seats info...
            </div>
        )
    }

    if(!seats) {
        return (
            <div className='message--info'>
                Sorry! Could not find any seats!
            </div>
        )
    }

    const seatsList: Seat[] = Object.values(seats);
    const rows = createArrayWithGivenSize(10);
    const columns = createArrayWithGivenSize(15);

    if(!seatsList || seatsList.length < 1) {
        return (
            <div className='message--info'>
                Sorry! Could not find any seats!
            </div>
        )
    }

    return (
        <SeatsGridComponent
            columns={columns}
            rows={rows}
            seats={seatsList}
            onSeatClick={onSeatClick}
        />
    )
}

const mapStateToProps = (store: any, ownProps: OwnProps) => {
    const state = store.screeningSeats;
    return {
        seats: state.seats,
        isFetching: state.isFetching,
        ...ownProps
    }
}

// @ts-ignore
const ConnectedReservationSeatsComponent: React.FC<OwnProps> = connect(mapStateToProps)(ReservationSeatsComponent);

export const ReservationSeats = (props: OwnProps) => {

    const { className } = props;

    return (
        <div className={className}>
            <TransformWrapper
                defaultPositionX={0}
                defaultPositionY={0}
                options={{
                    centerContent: true,
                    limitToBounds: false,
                    minScale: 0.5,
                    maxScale: 2.0
                }}
                doubleClick={{
                    disabled: false
                }}
                scale={0.6}>
                <TransformComponent>
                    <ConnectedReservationSeatsComponent {...props} />
                </TransformComponent>
            </TransformWrapper>
        </div>
    )
}