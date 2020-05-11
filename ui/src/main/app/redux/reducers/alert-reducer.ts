import {AlertAction, DismissAlertActionInterface, PushAlertActionInterface} from "../actions/alert";
import {alertConstants} from "../constants";
import {Alert} from "../../models/infrastructure";

const initialState = {};

export interface AlertState {
    [component: string]: Alert[];
}

export function alertReducer(state: AlertState = initialState, action: AlertAction): AlertState {
    let componentAlerts;
    switch(action.type) {
        case alertConstants.PUSH_ALERT:
            const { alertData } = (action as PushAlertActionInterface);
            const currentAlerts = state[alertData.component];
            if(currentAlerts && currentAlerts.filter(alert => alert.id === alertData.id).length > 0) {
                return state;
            }
            componentAlerts = currentAlerts ? [...state[alertData.component], alertData] : [alertData];
            return {
                ...state,
                [alertData.component]: componentAlerts
            };
        case alertConstants.DISMISS_ALERT:
            const { component, alertId } = (action as DismissAlertActionInterface);
            componentAlerts = state[component]
                ? state[component].filter(alert => alert.id !== alertId)
                : null;
            return componentAlerts ? {...state, [component]: componentAlerts} : state;
        default:
            return state;
    }
}