import { registerConstants } from '../../constants';
import { RegistrationData } from '../../../models/authorization';
import {Alert, Resource} from '../../../models/infrastructure';
import {Action, Dispatch} from 'redux'
import { AuthenticationService } from '../../../services';
import {
    RegisterFailureActionInterface,
    RegisterSuccessActionInterface,
    RegisterRequestActionInterface } from './types';
import {AlertPublisher, AlertPublisherImpl} from "../alert";
import {message} from "antd";

export interface RegisterActionPublisher {
    register(registrationData: RegistrationData, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void;
}

export class RegisterActionPublisherImpl implements RegisterActionPublisher {
    authService: AuthenticationService;
    alertPublisher: AlertPublisher;

    constructor(authService: AuthenticationService, alertPublisher?: AlertPublisher) {
        this.authService = authService;
        this.alertPublisher = alertPublisher ? alertPublisher : AlertPublisherImpl.createInstance();
    }

    register(registrationData: RegistrationData, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void {
        return (dispatch: Dispatch<Action>) => {
            dispatch(request(registrationData));

            this.authService.register(registrationData)
                .then(
                    (resource: Resource) => {
                        dispatch(success(resource));
                    },
                    (errorResponse: any) => {
                        dispatch(failure(errorResponse.message));
                        if (errorAlertSupplier) {
                            const alert = errorAlertSupplier(errorResponse.message);
                            this.alertPublisher.pushAlert(alert)(dispatch);
                        }
                    }
                );
        };

        function request(registrationData: RegistrationData): RegisterRequestActionInterface {
            return {
                type: registerConstants.REGISTER_REQUEST,
                registrationData: registrationData
            }
        }

        function success(resource: Resource): RegisterSuccessActionInterface {
            return {
                type: registerConstants.REGISTER_SUCCESS,
                resource: resource
            }
        }

        function failure(error: string): RegisterFailureActionInterface {
            return {
                type: registerConstants.REGISTER_FAILURE,
                error: error
            }
        }
    }
}