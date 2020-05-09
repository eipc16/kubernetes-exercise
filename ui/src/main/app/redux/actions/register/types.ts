import { Action } from 'redux';
import { registerConstants } from '../../constants';
import { RegistrationData } from '../../../models/authorization';
import { Resource } from '../../../models/infrastructure';

export interface RegisterRequestActionInterface extends Action {
    type: typeof registerConstants.REGISTER_REQUEST,
    registrationData: RegistrationData;
}

export interface RegisterFailureActionInterface extends Action {
    type: typeof registerConstants.REGISTER_FAILURE,
    error: string;
}

export interface RegisterSuccessActionInterface extends Action {
    type: typeof registerConstants.REGISTER_SUCCESS
    resource: Resource;
}

export type RegisterAction = RegisterRequestActionInterface | 
                             RegisterSuccessActionInterface | 
                             RegisterFailureActionInterface;
