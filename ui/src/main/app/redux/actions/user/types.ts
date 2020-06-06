import { Action } from 'redux'
import { userConstants } from '../../constants'
import { User } from '../../../models/users'

export interface CurrentUserRequestActionInterface extends Action {
    type: typeof userConstants.CURRENT_USER_REQUEST;
}

export interface CurrentUserSuccessActionInterface extends Action {
    type: typeof userConstants.CURRENT_USER_SUCCESS;
    userData: User;
}

export interface CurrentUserFailureActionInterface extends Action {
    type: typeof userConstants.CURRENT_USER_FAILURE;
    error: string;
}

export type CurrentUserAction = CurrentUserRequestActionInterface |
                                CurrentUserSuccessActionInterface |
                                CurrentUserFailureActionInterface;
