import { Action } from "redux";
import { alertConstants } from "../../constants";
import { Alert } from "../../../models/infrastructure";

export interface PushAlertActionInterface extends Action {
    type: typeof alertConstants.PUSH_ALERT;
    alertData: Alert;
}

export interface DismissAlertActionInterface extends Action {
    type: typeof alertConstants.DISMISS_ALERT;
    component: string;
    alertId: string;
}

export type AlertAction =   PushAlertActionInterface |
                            DismissAlertActionInterface;