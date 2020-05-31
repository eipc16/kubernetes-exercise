import React, {useEffect, useState} from "react";
import {connect, useDispatch} from "react-redux";
import {Button, List, Pagination} from "antd";
import moment from 'moment';

import {ScreeningActionPublisher} from "../../../redux/actions/screening";
import {useFetching} from "../../../utils/custom-fetch-hook";
import {Screening} from "../../../models/screening";
import {ScreeningsState} from "../../../redux/reducers/screening-reducer";

import './reservation-date-selector.scss'

interface ReservationDatePickerProps {
    formattedDates: string[];
    onSelectDate: (formattedDate: string) => void;
}

interface ScreeningsByDay {
    [formattedDate: string]: Screening[];
}

interface GroupedScreeningsWithDates {
    groupedScreenings: ScreeningsByDay;
    formattedDates: string[];
}

type PaginationActions = 'page' | 'prev' | 'next' | 'jump-prev' | 'jump-next';

const ReservationDatePicker = (props: ReservationDatePickerProps) => {
    const { formattedDates, onSelectDate } = props;

    const customPaginationRender = (page: number, type: PaginationActions, originalElement: React.ReactElement<HTMLElement>): React.ReactNode => {
        switch (type) {
            case 'page':
                return <Button>{formattedDates[page - 1]}</Button>
            case 'prev':
                return <Button>Previous</Button>
            case 'next':
                return <Button>Next</Button>
            default:
                return originalElement;
        }
    }

    const onChange = (page: number) => {
        onSelectDate(formattedDates[page - 1])
    }

    // eslint-disable-next-line
    useEffect(() => onChange(1), [])

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
}

interface ScreeningSelectorProps {
    screenings: Screening[];
    currentScreening?: Screening;
    onSelectScreening: (screening: Screening) => void;
    dateFormat?: string;
}

const ScreeningSelector = (props: ScreeningSelectorProps) => {
    const { screenings, dateFormat, currentScreening, onSelectScreening } = props;

    const currentDateTimeFormat: string = dateFormat || 'hh:mm';

    const onScreeningClick = (e: React.MouseEvent<HTMLElement>, screening: Screening) => {
        e.preventDefault();
        onSelectScreening(screening);
    }

    const isSelected = (screening: Screening) => {
        return currentScreening && currentScreening.screeningId === screening.screeningId;
    }

    return (
        <div>
            <List dataSource={screenings} renderItem={ screening =>
                <Button className={`single--screening ${isSelected(screening) && 'selected-screening'}`}
                        key={screening.screeningId}
                        onClick={(e) => onScreeningClick(e, screening)}
                >
                    {moment(screening.startTime).format(currentDateTimeFormat)} - Room: {screening.screeningRoom.number}
                </Button>
            }>
            </List>
        </div>
    )
}

interface OwnProps {
    className: string;
    movieId: number;
    screeningActionPublisher: ScreeningActionPublisher;
    dateFormat?: string;
}

interface ReservationDateSelectorState {
    screenings: Screening[];
    currentScreening?: Screening;
    isFetching: boolean;
}

export type ReservationDateSelectorProps = OwnProps & ReservationDateSelectorState;

const ReservationDateSelectorComponent = (props: ReservationDateSelectorProps) => {
    const dispatch = useDispatch();
    const {className, movieId, screeningActionPublisher, isFetching, screenings, dateFormat, currentScreening } = props;
    const [visibleScreenings, setVisibleScreenings] = useState(([] as Screening[]));
    useFetching(screeningActionPublisher.fetchScreenings(movieId), [movieId]);

    if (isFetching) {
        return (
            <div className='info--message'>
                Fetching screenings...
            </div>
        )
    }

    const currentDateTimeFormat: string = dateFormat || 'DD/MM/YYYY';

    const getSortedScreenings = (screeningsToSort: Screening[]) => {
        const sortingRule = (first: Screening, second: Screening) => {
            return new Date(first.startTime).getTime() - new Date(second.startTime).getTime();
        }
        const tempScreenings = Object.assign([], screeningsToSort);
        tempScreenings.sort(sortingRule);
        return tempScreenings;
    }

    const getScreeningsByDate = (screenings: Screening[]): GroupedScreeningsWithDates => {
        const dates: string[] = [];
        const screeningsByDay: ScreeningsByDay = getSortedScreenings(screenings).reduce((acc: ScreeningsByDay, screening: Screening) => {
            const formattedDate: string = moment(screening.startTime).format(currentDateTimeFormat);
            if (!dates.includes(formattedDate)) {
                dates.push(formattedDate);
            }
            if (acc[formattedDate]) {
                acc[formattedDate].push(screening)
            } else {
                acc[formattedDate] = [screening]
            }
            return acc;
        }, {})
        return {
            groupedScreenings: screeningsByDay,
            formattedDates: dates
        }
    }

    const {groupedScreenings, formattedDates} = getScreeningsByDate(screenings);

    const onSelectedDate = (date: string) => {
        setVisibleScreenings(groupedScreenings[date]);
    }

    const onSelectScreening = (screening: Screening) => {
        dispatch(screeningActionPublisher.setCurrentScreening(screening.screeningId))
    }

    return (
        <div className={className}>
            <div className='date--selector'>
                <ReservationDatePicker formattedDates={formattedDates} onSelectDate={onSelectedDate}/>
                { visibleScreenings ? (
                    <ScreeningSelector screenings={visibleScreenings} currentScreening={currentScreening} onSelectScreening={onSelectScreening}/>
                ) : (
                    <div className='info--message'>No screenings for given date</div>
                )}
            </div>
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