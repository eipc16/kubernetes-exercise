import { registerConstants } from '../../constants';
import { RegistrationData } from '../../../models/authorization';
import { Resource } from '../../../models/infrastructure';
import { Dispatch } from 'redux'
import { AuthenticationService } from '../../../services';
import {
    RegisterAction, 
    RegisterFailureActionInterface,
    RegisterSuccessActionInterface,
    RegisterRequestActionInterface } from './types';

export interface RegisterActionPublisher {
    register(registrationData: RegistrationData): (dispatch: Dispatch<RegisterAction>) => void;
}

export class RegisterActionPublisherImpl implements RegisterActionPublisher {
    authService: AuthenticationService;    

    constructor(authService: AuthenticationService) {
        this.authService = authService;
    }

    register(registrationData: RegistrationData): (dispatch: Dispatch<RegisterAction>) => void {
        return (dispatch: Dispatch<RegisterAction>) => {
            dispatch(request(registrationData));

            this.authService.register(registrationData)
                .then(
                    (resource: Resource) => {
                        dispatch(success(resource));
                    },
                    (errorResponse: any) => {
                        dispatch(failure(errorResponse.message));
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