import { loginConstants } from '../../constants';
import { LoginData, Token } from '../../../models/authorization';
import { Dispatch } from 'redux'
import { AuthenticationService } from '../../../services';
import {
    LoginAction, 
    LogoutAction,
    LoginRequestActionInterface,
    LoginSuccessActionInterface,
    LoginFailureActionInterface } from './types';

export interface LoginActionPublisher {
    login(loginData: LoginData): (dispatch: Dispatch<LoginAction>) => void;
    logout(): LogoutAction;
}

export class LoginActionPublisherImpl implements LoginActionPublisher {
    authService: AuthenticationService;    

    constructor(authService: AuthenticationService) {
        this.authService = authService;
    }

    login(loginData: LoginData): (dispatch: Dispatch<LoginAction>) => void {
        return (dispatch: Dispatch<LoginAction>) => {
            dispatch(request(loginData));

            this.authService.login(loginData)
                .then(
                    (token: Token) => {
                        dispatch(success(token));
                    },
                    (errorResponse: any) => {
                        dispatch(failure(errorResponse.message));
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