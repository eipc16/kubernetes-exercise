import {AlertAction, DismissAlertActionInterface, PushAlertActionInterface} from '../actions/alert'
import {alertConstants} from '../constants'
import {Alert} from '../../models/infrastructure'

const initialState = {}

export interface AlertState {
    [component: string]: Alert[];
}

function pushAlert(state: AlertState, action: PushAlertActionInterface): AlertState {
    const {alertData} = action;
    const currentAlerts = state[alertData.component];
    if (currentAlerts && currentAlerts.filter(alert => alert.id === alertData.id).length > 0) {
        return state
    }
    const componentAlerts = currentAlerts ? [...state[alertData.component], alertData] : [alertData];
    return {
        ...state,
        [alertData.component]: componentAlerts
    };
}

function dismissAlert(state: AlertState, action: DismissAlertActionInterface): AlertState {
    const {component, alertId} = action;
    const componentAlerts = state[component]
        ? state[component].filter(alert => alert.id !== alertId)
        : null;
    return componentAlerts ? {...state, [component]: componentAlerts} : state;
}

export function alertReducer(state: AlertState = initialState, action: AlertAction): AlertState {
    switch (action.type) {
        case alertConstants.PUSH_ALERT:
            return pushAlert(state, (action as PushAlertActionInterface));
        case alertConstants.DISMISS_ALERT:
            return dismissAlert(state, (action as DismissAlertActionInterface));
        default:
            return state
    }
}
