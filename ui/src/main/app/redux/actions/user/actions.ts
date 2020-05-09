import { userConstants } from '../../constants';
import { User } from '../../../models/users';
import { Dispatch } from 'redux'
import { UserService } from '../../../services';
import {
    CurrentUserAction,
    CurrentUserFailureActionInterface,
    CurrentUserRequestActionInterface,
    CurrentUserSuccessActionInterface
} from './types';

export interface UserActionPublisher {
    getCurrentUser(): (dispatch: Dispatch<CurrentUserAction>) => void;
}

export class UserActionPublisherImpl implements UserActionPublisher {
    userService: UserService;

    constructor(userService: UserService) {
        this.userService = userService;
    }

    getCurrentUser(): (dispatch: Dispatch<CurrentUserAction>) => void {
        return (dispatch: Dispatch<CurrentUserAction>) => {
            dispatch(request());

            this.userService.getCurrentUser()
                .then(
                    (userData: User) => {
                        dispatch(success(userData));
                    },
                    (errorResponse: any) => {
                        dispatch(failure(errorResponse.message));
                    }
                );
        };

        function request(): CurrentUserRequestActionInterface {
            return {
                type: userConstants.CURRENT_USER_REQUEST
            }
        }

        function success(userData: User): CurrentUserSuccessActionInterface {
            return {
                type: userConstants.CURRENT_USER_SUCCESS,
                userData: userData
            }
        }

        function failure(error: string): CurrentUserFailureActionInterface {
            return {
                type: userConstants.CURRENT_USER_FAILURE,
                error: error
            }
        }
    }
}