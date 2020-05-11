import { registerConstants } from '../constants';
import {RegisterAction} from "../actions/register";

const initialState = {};

export interface RegistrationState {
    registered?: boolean;
    registering?: boolean;
}

export function registrationReducer(state: RegistrationState = initialState, action: RegisterAction): RegistrationState {
    switch(action.type) {
        case registerConstants.REGISTER_REQUEST:
            return {
                registering: true,
            };
        case registerConstants.REGISTER_SUCCESS:
            return {
                registered: true,
            };
        case registerConstants.REGISTER_FAILURE:
            return {};
        default:
            return state;
    }
}