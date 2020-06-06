import { Action } from 'redux'
import { loginConstants } from '../../constants'
import { LoginData, Token } from '../../../models/authorization'

export interface LoginRequestActionInterface extends Action {
    type: typeof loginConstants.LOGIN_REQUEST;
    userData: LoginData;
}

export interface LoginFailureActionInterface extends Action {
    type: typeof loginConstants.LOGIN_FAILURE;
    error: string;
}

export interface LoginSuccessActionInterface extends Action {
    type: typeof loginConstants.LOGIN_SUCCESS;
    token: Token;
}

export interface LogoutActionInterface extends Action {
    type: typeof loginConstants.LOGOUT;
}

export type LoginAction = LoginSuccessActionInterface | LoginRequestActionInterface | LoginFailureActionInterface;
export type LogoutAction = LogoutActionInterface;
