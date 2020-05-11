import { loginConstants } from '../../constants';
import { LoginData, Token } from '../../../models/authorization';
import {Action, Dispatch} from 'redux'
import { AuthenticationService } from '../../../services';
import {
    LogoutAction,
    LoginRequestActionInterface,
    LoginSuccessActionInterface,
    LoginFailureActionInterface } from './types';
import {AlertPublisher, AlertPublisherImpl} from "../alert";
import {Alert} from "../../../models/infrastructure";

export interface LoginActionPublisher {
    login(loginData: LoginData, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void;
    logout(): LogoutAction;
}

export class LoginActionPublisherImpl implements LoginActionPublisher {
    authService: AuthenticationService;
    alertPublisher: AlertPublisher;

    constructor(authService: AuthenticationService) {
        this.authService = authService;
        this.alertPublisher = AlertPublisherImpl.createInstance();
    }

    login(loginData: LoginData, errorAlertSupplier?: (message: string) => Alert): (dispatch: Dispatch<Action>) => void {
        return (dispatch: Dispatch<Action>) => {
            dispatch(request(loginData));

            this.authService.login(loginData)
                .then(
                    (token: Token) => {
                        dispatch(success(token));
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

        function request(loginData: LoginData): LoginRequestActionInterface {
            return {
                type: loginConstants.LOGIN_REQUEST,
                userData: loginData
            }
        }

        function success(token: Token): LoginSuccessActionInterface {
            return {
                type: loginConstants.LOGIN_SUCCESS,
                token: token
            }
        }

        function failure(error: string): LoginFailureActionInterface {
            return {
                type: loginConstants.LOGIN_FAILURE,
                error: error
            }
        }
    }

    logout(): LogoutAction {
        this.authService.logout();
        return {
            type: loginConstants.LOGOUT
        }
    }
}