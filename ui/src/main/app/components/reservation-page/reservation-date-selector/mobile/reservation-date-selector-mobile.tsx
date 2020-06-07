import React, {CSSProperties, useState} from 'react';
import {GroupedScreeningsWithDates, ReservationDateSelectorProps} from "../reservation-date-selector";
import {DatePicker, Select } from "antd";
import moment from "moment";
import {useDispatch} from "react-redux";

import './reservation-date-selector-mobile.scss';

interface OwnProps {
    screeningsWithDates: GroupedScreeningsWithDates;
}

export type ReservationDateSelectorMobileProps = OwnProps & ReservationDateSelectorProps;

export const ReservationDateSelectorMobileComponent = (props: ReservationDateSelectorMobileProps): JSX.Element => {
    const dispatch = useDispatch();
    const { screeningsWithDates, dateFormat, screeningActionPublisher } = props;
    const { groupedScreenings, formattedDates } = screeningsWithDates;

    const [selectedScreeningIndex, setSelectedScreeningIndex] = useState(0);
    const [visibleScreenings, setVisibleScreenings] = useState(
        formattedDates.length > 0 ? groupedScreenings[formattedDates[0]] : []
    );

    const currentDateTimeFormat: string = dateFormat || 'DD/MM/YYYY';
    const timeFormat = 'HH:mm';

    const isDateDisabled = (moment: moment.Moment): boolean => {
        return moment.isBefore(moment.fromNow()) || !formattedDates.includes(moment.format(currentDateTimeFormat));
    };

    const onSelectScreening = (selectedValueIndex: number): void => {
        setSelectedScreeningIndex(selectedValueIndex);
        const screening = visibleScreenings[selectedValueIndex];
        console.log(screening);
        dispatch(screeningActionPublisher.setCurrentScreening(screening.screeningId))
    };

    const onDateSelect = (moment: moment.Moment | null): void => {
        if(moment) {
            setVisibleScreenings(groupedScreenings[moment.format(currentDateTimeFormat)]);
            onSelectScreening(0);
        }
    };

    const getDateStyle = (): CSSProperties => {
        return {
            textAlign: 'center'
        }
    };

    return (

        <div id='date--selector--mobile' className='date--selector'>
            <div className='screening--filter'>
                <label className='label' htmlFor='date--picker'>Screening date:</label>
                <DatePicker
                    id='date--picker'
                    className='date--picker--input'
                    defaultValue={
                        moment(formattedDates[0]).utc()
                    }
                    style={getDateStyle()}
                    disabledDate={isDateDisabled}
                    onChange={onDateSelect}
                />
            </div>
            <div className='screening--filter'>
                <label className='label' htmlFor='screening--picker'>Screening:</label>
                <Select
                    id='screening--picker'
                    className='screening--picker--input'
                    value={selectedScreeningIndex}
                    onChange={onSelectScreening}
                >
                    {
                        !visibleScreenings ? "No screenings :(" :
                            visibleScreenings.map((screening, index) => (
                                <Select.Option key={screening.screeningId} value={index}>
                                    {moment(screening.startTime).format(timeFormat)} - Room: {screening.screeningRoom.number}
                                </Select.Option>
                            ))
                    }
                </Select>
            </div>
        </div>
    )
};