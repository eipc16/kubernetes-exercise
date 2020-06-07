import React, {useEffect, useState} from "react";
import {useDispatch} from "react-redux";
import {Button, List, Pagination} from "antd";
import moment from 'moment';
import {Screening} from "../../../../models/screening";

import './reservation-date-selector-desktop.scss'
import {GroupedScreeningsWithDates, ReservationDateSelectorProps} from "../reservation-date-selector";

interface ReservationDatePickerProps {
    formattedDates: string[];
    onSelectDate: (formattedDate: string) => void;
}

type PaginationActions = 'page' | 'prev' | 'next' | 'jump-prev' | 'jump-next';

const ReservationDatePicker = (props: ReservationDatePickerProps): JSX.Element => {
    const {formattedDates, onSelectDate} = props;

    const customPaginationRender = (page: number, type: PaginationActions, originalElement: React.ReactElement<HTMLElement>): React.ReactNode => {
        switch (type) {
            case 'page':
                return <Button>{formattedDates[page - 1]}</Button>;
            case 'prev':
                return <Button>Previous</Button>;
            case 'next':
                return <Button>Next</Button>;
            default:
                return originalElement;
        }
    };

    const onChange = (page: number): void => {
        onSelectDate(formattedDates[page - 1])
    };

    // eslint-disable-next-line
    useEffect(() => onChange(1), []);

    return (
        <Pagination
            total={formattedDates.length}
            pageSize={1}
            itemRender={customPaginationRender}
            onChange={onChange}
            showSizeChanger={false}
            responsive={true}
        />
    )
};

interface ScreeningSelectorProps {
    screenings: Screening[];
    currentScreening?: Screening;
    onSelectScreening: (screening: Screening) => void;
    dateFormat?: string;
}

const ScreeningSelector = (props: ScreeningSelectorProps): JSX.Element => {
    const {screenings, dateFormat, currentScreening, onSelectScreening} = props;

    const currentDateTimeFormat: string = dateFormat || 'HH:mm';

    const onScreeningClick = (e: React.MouseEvent<HTMLElement>, screening: Screening): void => {
        e.preventDefault();
        onSelectScreening(screening);
    };

    const isSelected = (screening: Screening): boolean => {
        return currentScreening ? currentScreening.screeningId === screening.screeningId : false;
    };

    return (
        <div>
            <List dataSource={screenings} renderItem={(screening: Screening): JSX.Element =>
                <Button className={`single--screening ${isSelected(screening) && 'selected-screening'}`}
                        key={screening.screeningId}
                        onClick={(e: React.MouseEvent<HTMLElement, MouseEvent>): void => onScreeningClick(e, screening)}
                >
                    {moment(screening.startTime).format(currentDateTimeFormat)} - Room: {screening.screeningRoom.number}
                </Button>
            }>
            </List>
        </div>
    )
};

interface OwnProps {
    screeningsWithDates: GroupedScreeningsWithDates;
}

export type ReservationDateSelectorDesktopProps = OwnProps & ReservationDateSelectorProps;

export const ReservationDateSelectorDesktopComponent = (props: ReservationDateSelectorDesktopProps): JSX.Element => {
    const dispatch = useDispatch();
    const {screeningActionPublisher, screeningsWithDates, currentScreening} = props;
    const [visibleScreenings, setVisibleScreenings] = useState(([] as Screening[]));
    const {groupedScreenings, formattedDates} = screeningsWithDates;

    const onSelectedDate = (date: string): void => {
        setVisibleScreenings(groupedScreenings[date]);
    };

    const onSelectScreening = (screening: Screening): void => {
        dispatch(screeningActionPublisher.setCurrentScreening(screening.screeningId))
    };

    return (
        <div id='date--selector--desktop' className='date--selector'>
            {
                screeningsWithDates.formattedDates.length > 0 ? (
                    <ReservationDatePicker formattedDates={formattedDates} onSelectDate={onSelectedDate}/>
                ) : null
            }
            {visibleScreenings ? (
                <ScreeningSelector screenings={visibleScreenings} currentScreening={currentScreening}
                                   onSelectScreening={onSelectScreening}/>
            ) : (
                <div className='info--message'>No screenings for given date</div>
            )}
        </div>
    )
};