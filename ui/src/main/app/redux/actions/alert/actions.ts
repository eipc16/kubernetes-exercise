import {Alert} from "../../../models/infrastructure";
import {AlertAction, DismissAlertActionInterface, PushAlertActionInterface} from "./types";
import {alertConstants} from "../../constants";
import {AlertInterface} from "../../../models/infrastructure/Alert";
import {Dispatch} from "redux";

export interface AlertPublisher {
    pushAlert(alert: Alert): (dispatch: Dispatch<AlertAction>) => void;
    dismissAlert(component:string, alertId: string): DismissAlertActionInterface;
}

export class AlertPublisherImpl implements AlertPublisher {

    static publisherInstance: AlertPublisher;

    static createInstance(): AlertPublisher {
        if (!AlertPublisherImpl.publisherInstance) {
            AlertPublisherImpl.publisherInstance = new AlertPublisherImpl();
        }
        return AlertPublisherImpl.publisherInstance;
    }

    pushAlert(alert: AlertInterface): (dispatch: Dispatch<AlertAction>) => void {
        return dispatch => {
            dispatch(AlertPublisherImpl.pushAlertAction(alert));
            if(alert.duration && alert.duration > 0) {
                setTimeout(() => dispatch(this.dismissAlert(alert.component, alert.id)), alert.duration * 1000);
            }
        };
    }

    private static pushAlertAction(alert: AlertInterface): PushAlertActionInterface {
        return {
            type: alertConstants.PUSH_ALERT,
            alertData: alert
        };
    }

    dismissAlert(component:string, alertId: string): DismissAlertActionInterface {
        return {
            type: alertConstants.DISMISS_ALERT,
            component: component,
            alertId: alertId
        };
    }
}