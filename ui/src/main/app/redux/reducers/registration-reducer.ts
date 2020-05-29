import { registerConstants } from '../constants'
import { RegisterAction, RegisterSuccessActionInterface } from '../actions/register'
import { Resource } from '../../models/infrastructure'

const initialState = {}

export interface RegistrationState {
    registered?: boolean;
    registering?: boolean;
    resource?: Resource;
}

export function registrationReducer (state: RegistrationState = initialState, action: RegisterAction): RegistrationState {
  switch (action.type) {
    case registerConstants.REGISTER_REQUEST:
      return {
        registering: true
      }
    case registerConstants.REGISTER_SUCCESS:
      return {
        registered: true,
        resource: (action as RegisterSuccessActionInterface).resource
      }
    case registerConstants.REGISTER_FAILURE:
      return {}
    default:
      return state
  }
}
